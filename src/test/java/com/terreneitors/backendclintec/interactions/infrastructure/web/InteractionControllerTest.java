package com.terreneitors.backendclintec.interactions.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.terreneitors.backendclintec.interactions.application.port.in.InteractionCrudUseCase;
import com.terreneitors.backendclintec.interactions.domain.Interaction;
import com.terreneitors.backendclintec.interactions.domain.TypeInteraction;
import com.terreneitors.backendclintec.interactions.infrastructure.dto.InteractionRequestDTO;
import com.terreneitors.backendclintec.interactions.infrastructure.dto.InteractionResponseDTO;
import com.terreneitors.backendclintec.interactions.infrastructure.persistence.Mapper.InteractionPersistenceMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InteractionController.class)
class InteractionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @MockitoBean
    private InteractionCrudUseCase useCase;

    @MockitoBean
    private InteractionPersistenceMapper mapper;

    @MockitoBean
    private com.terreneitors.backendclintec.security.JwtService jwtService;

    @MockitoBean
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    // ── Helpers ─────────────────────────────────────────────────────────────

    private Interaction crearInteractionMock(Long id, Long clienteId, Long usuarioId, Long oportunidadId) {
        Interaction interaction = new Interaction();
        interaction.setId(id);
        interaction.setClienteId(clienteId);
        interaction.setUsuarioId(usuarioId);
        interaction.setOportunidadId(oportunidadId);
        return interaction;
    }

    private InteractionResponseDTO crearResponseDTOMock(Long id, Long clienteId, Long usuarioId, Long oportunidadId) {
        return new InteractionResponseDTO(
                id,
                clienteId,
                usuarioId,
                oportunidadId,
                TypeInteraction.LLAMADA,       // ← ajusta al valor real del enum
                "Nota de prueba",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private InteractionRequestDTO crearRequestDTOValido() {
        return new InteractionRequestDTO(
                1L,
                2L,
                3L,
                TypeInteraction.LLAMADA,       // ← ajusta al valor real del enum
                "Nota de prueba"
        );
    }

    // ── GET /api/interacciones ───────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("GET /api/interacciones - Debería retornar la lista de interacciones con estado 200 OK")
    void listar_ShouldReturnInteractionsList() throws Exception {
        Interaction i1 = crearInteractionMock(1L, 1L, 2L, 3L);
        Interaction i2 = crearInteractionMock(2L, 4L, 5L, 6L);
        InteractionResponseDTO dto1 = crearResponseDTOMock(1L, 1L, 2L, 3L);
        InteractionResponseDTO dto2 = crearResponseDTOMock(2L, 4L, 5L, 6L);

        when(useCase.findAll()).thenReturn(List.of(i1, i2));
        when(mapper.toDTO(i1)).thenReturn(dto1);
        when(mapper.toDTO(i2)).thenReturn(dto2);

        mockMvc.perform(get("/api/interacciones"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    // ── GET /api/interacciones/{id} ──────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ASESOR")
    @DisplayName("GET /api/interacciones/{id} - Debería retornar una interacción si existe")
    void buscarPorId_WhenExists_ShouldReturnInteraction() throws Exception {
        Long id = 1L;
        Interaction interaction = crearInteractionMock(id, 1L, 2L, 3L);
        InteractionResponseDTO dto = crearResponseDTOMock(id, 1L, 2L, 3L);

        when(useCase.findById(id)).thenReturn(Optional.of(interaction));
        when(mapper.toDTO(interaction)).thenReturn(dto);

        mockMvc.perform(get("/api/interacciones/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.clienteId").value(1L))
                .andExpect(jsonPath("$.usuarioId").value(2L));
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    @DisplayName("GET /api/interacciones/{id} - Debería retornar 404 si la interacción no existe")
    void buscarPorId_WhenNotExists_ShouldReturnNotFound() throws Exception {
        Long id = 99L;
        when(useCase.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/interacciones/{id}", id))
                .andExpect(status().isNotFound());
    }

    // ── GET /api/interacciones/cliente/{clienteId} ───────────────────────────

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("GET /api/interacciones/cliente/{clienteId} - Debería retornar las interacciones de un cliente")
    void porCliente_ShouldReturnList() throws Exception {
        Long clienteId = 1L;
        Interaction interaction = crearInteractionMock(1L, clienteId, 2L, 3L);
        InteractionResponseDTO dto = crearResponseDTOMock(1L, clienteId, 2L, 3L);

        when(useCase.findByIdClient(clienteId)).thenReturn(List.of(interaction));
        when(mapper.toDTO(interaction)).thenReturn(dto);

        mockMvc.perform(get("/api/interacciones/cliente/{clienteId}", clienteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].clienteId").value(clienteId));
    }

    // ── GET /api/interacciones/oportunidad/{oportunidadId} ───────────────────

    @Test
    @WithMockUser(roles = "GERENTE")
    @DisplayName("GET /api/interacciones/oportunidad/{oportunidadId} - Debería retornar las interacciones de una oportunidad")
    void porOportunidad_ShouldReturnList() throws Exception {
        Long oportunidadId = 3L;
        Interaction interaction = crearInteractionMock(1L, 1L, 2L, oportunidadId);
        InteractionResponseDTO dto = crearResponseDTOMock(1L, 1L, 2L, oportunidadId);

        when(useCase.findByIdOpportunities(oportunidadId)).thenReturn(List.of(interaction));
        when(mapper.toDTO(interaction)).thenReturn(dto);

        mockMvc.perform(get("/api/interacciones/oportunidad/{oportunidadId}", oportunidadId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].oportunidadId").value(oportunidadId));
    }

    // ── GET /api/interacciones/usuario/{usuarioId} ───────────────────────────

    @Test
    @WithMockUser(roles = "ASESOR")
    @DisplayName("GET /api/interacciones/usuario/{usuarioId} - Debería retornar las interacciones de un usuario")
    void porUsuario_ShouldReturnList() throws Exception {
        Long usuarioId = 2L;
        Interaction interaction = crearInteractionMock(1L, 1L, usuarioId, 3L);
        InteractionResponseDTO dto = crearResponseDTOMock(1L, 1L, usuarioId, 3L);

        when(useCase.findByIdUser(usuarioId)).thenReturn(List.of(interaction));
        when(mapper.toDTO(interaction)).thenReturn(dto);

        mockMvc.perform(get("/api/interacciones/usuario/{usuarioId}", usuarioId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].usuarioId").value(usuarioId));
    }

    // ── POST /api/interacciones ──────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("POST /api/interacciones - Debería crear una interacción exitosamente")
    void crear_WithValidDTO_ShouldReturnCreated() throws Exception {
        InteractionRequestDTO requestDTO = crearRequestDTOValido();
        Interaction creada = crearInteractionMock(1L, 1L, 2L, 3L);
        InteractionResponseDTO responseDTO = crearResponseDTOMock(1L, 1L, 2L, 3L);

        when(useCase.createInteraction(any(InteractionRequestDTO.class))).thenReturn(creada);
        when(mapper.toDTO(creada)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/interacciones")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.clienteId").value(1L));
    }

    @Test
    @WithMockUser(roles = "ASESOR")
    @DisplayName("POST /api/interacciones - Debería retornar 400 si el DTO no es válido")
    void crear_WithInvalidDTO_ShouldReturnBadRequest() throws Exception {
        // clienteId, usuarioId, oportunidadId y tipo nulos → viola @NotNull
        InteractionRequestDTO invalidRequest = new InteractionRequestDTO(
                null,
                null,
                null,
                null,
                "x".repeat(1001)   // supera @Size(max=1000)
        );

        mockMvc.perform(post("/api/interacciones")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(useCase, never()).createInteraction(any());
    }

    // ── PUT /api/interacciones/{id} ──────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("PUT /api/interacciones/{id} - Debería actualizar una interacción correctamente")
    void actualizar_WithValidDTO_ShouldReturnOk() throws Exception {
        Long id = 1L;
        InteractionRequestDTO requestDTO = crearRequestDTOValido();
        Interaction actualizada = crearInteractionMock(id, 1L, 2L, 3L);
        InteractionResponseDTO responseDTO = crearResponseDTOMock(id, 1L, 2L, 3L);

        when(useCase.updateInteraction(eq(id), any(InteractionRequestDTO.class))).thenReturn(actualizada);
        when(mapper.toDTO(actualizada)).thenReturn(responseDTO);

        mockMvc.perform(put("/api/interacciones/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nota").value("Nota de prueba"));
    }

    @Test
    @WithMockUser(roles = "ASESOR")
    @DisplayName("PUT /api/interacciones/{id} - Debería retornar 400 si el DTO no es válido en la actualización")
    void actualizar_WithInvalidDTO_ShouldReturnBadRequest() throws Exception {
        Long id = 1L;
        InteractionRequestDTO invalidRequest = new InteractionRequestDTO(
                null,
                null,
                null,
                null,
                "x".repeat(1001)
        );

        mockMvc.perform(put("/api/interacciones/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(useCase, never()).updateInteraction(any(), any());
    }
}