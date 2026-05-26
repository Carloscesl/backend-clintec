package com.terreneitors.backendclintec.opportunities.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.terreneitors.backendclintec.opportunities.application.port.in.OpportunityCrudUseCase;
import com.terreneitors.backendclintec.opportunities.domain.Opportunity;
import com.terreneitors.backendclintec.opportunities.domain.StageOpportunity;
import com.terreneitors.backendclintec.opportunities.domain.StatusOpportunity;
import com.terreneitors.backendclintec.opportunities.infrastructure.dto.OpportunityRequestDTO;
import com.terreneitors.backendclintec.opportunities.infrastructure.dto.OpportunityResponseDTO;
import com.terreneitors.backendclintec.opportunities.infrastructure.persistence.mapper.OpportunityPersistenceMapper;import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OpportunityController.class)
class OpportunityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // JavaTimeModule registrado para serializar LocalDate / LocalDateTime
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @MockitoBean
    private OpportunityCrudUseCase oportunidadacaseUse;

    @MockitoBean
    private OpportunityPersistenceMapper mapper;

    @MockitoBean
    private com.terreneitors.backendclintec.security.JwtService jwtService;

    @MockitoBean
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    // ── Helpers ─────────────────────────────────────────────────────────────

    private Opportunity crearOpportunityMock(Long id, Long clienteId, Long asesorId) {
        Opportunity op = new Opportunity();
        op.setIdOportunidad(id);
        op.setClienteId(clienteId);
        op.setAsesorId(asesorId);
        return op;
    }

    private OpportunityResponseDTO crearResponseDTOMock(Long id, Long clienteId, Long asesorId) {
        return new OpportunityResponseDTO(
                id,
                clienteId,
                asesorId,
                "Descripción de prueba",
                new BigDecimal("5000000"),
                75,
                StageOpportunity.PROPUESTA,       // ajusta al valor real del enum
                StatusOpportunity.ACTIVA,         // ajusta al valor real del enum
                false,
                null,
                LocalDate.now().plusMonths(3),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private OpportunityRequestDTO crearRequestDTOValido() {
        return new OpportunityRequestDTO(
                1L,
                2L,
                "Descripción de prueba",
                new BigDecimal("5000000"),
                LocalDate.now().plusMonths(3)
        );
    }

    // ── GET /api/oportunidades ───────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("GET /api/oportunidades - Debería retornar la lista de oportunidades con estado 200 OK")
    void list_ShouldReturnOpportunitiesList() throws Exception {
        Opportunity op1 = crearOpportunityMock(1L, 1L, 2L);
        Opportunity op2 = crearOpportunityMock(2L, 3L, 4L);
        OpportunityResponseDTO dto1 = crearResponseDTOMock(1L, 1L, 2L);
        OpportunityResponseDTO dto2 = crearResponseDTOMock(2L, 3L, 4L);

        when(oportunidadacaseUse.findAll()).thenReturn(List.of(op1, op2));
        when(mapper.toDTO(op1)).thenReturn(dto1);
        when(mapper.toDTO(op2)).thenReturn(dto2);

        mockMvc.perform(get("/api/oportunidades"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].idOportunidad").value(1L))
                .andExpect(jsonPath("$[1].idOportunidad").value(2L));
    }

    // ── GET /api/oportunidades/id/{id} ──────────────────────────────────────

    @Test
    @WithMockUser(roles = "ASESOR")
    @DisplayName("GET /api/oportunidades/id/{id} - Debería retornar una oportunidad si existe")
    void findById_WhenExists_ShouldReturnOpportunity() throws Exception {
        Long id = 1L;
        Opportunity op = crearOpportunityMock(id, 1L, 2L);
        OpportunityResponseDTO dto = crearResponseDTOMock(id, 1L, 2L);

        when(oportunidadacaseUse.findById(id)).thenReturn(Optional.of(op));
        when(mapper.toDTO(op)).thenReturn(dto);

        mockMvc.perform(get("/api/oportunidades/id/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idOportunidad").value(id))
                .andExpect(jsonPath("$.clienteId").value(1L));
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    @DisplayName("GET /api/oportunidades/id/{id} - Debería retornar 404 si la oportunidad no existe")
    void findById_WhenNotExists_ShouldReturnNotFound() throws Exception {
        Long id = 99L;
        when(oportunidadacaseUse.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/oportunidades/id/{id}", id))
                .andExpect(status().isNotFound());
    }

    // ── GET /api/oportunidades/idasesor/{id} ────────────────────────────────

    @Test
    @WithMockUser(roles = "ASESOR")
    @DisplayName("GET /api/oportunidades/idasesor/{id} - Debería retornar las oportunidades de un asesor")
    void findByIdAssessor_ShouldReturnList() throws Exception {
        Long asesorId = 2L;
        Opportunity op = crearOpportunityMock(1L, 1L, asesorId);
        OpportunityResponseDTO dto = crearResponseDTOMock(1L, 1L, asesorId);

        when(oportunidadacaseUse.findByIdAssessor(asesorId)).thenReturn(List.of(op));
        when(mapper.toDTO(op)).thenReturn(dto);

        mockMvc.perform(get("/api/oportunidades/idasesor/{id}", asesorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].asesorId").value(asesorId));
    }

    // ── GET /api/oportunidades/idcliente/{id} ───────────────────────────────

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("GET /api/oportunidades/idcliente/{id} - Debería retornar las oportunidades de un cliente")
    void findByIdClient_ShouldReturnList() throws Exception {
        Long clienteId = 1L;
        Opportunity op = crearOpportunityMock(1L, clienteId, 2L);
        OpportunityResponseDTO dto = crearResponseDTOMock(1L, clienteId, 2L);

        when(oportunidadacaseUse.findByIdClient(clienteId)).thenReturn(List.of(op));
        when(mapper.toDTO(op)).thenReturn(dto);

        mockMvc.perform(get("/api/oportunidades/idcliente/{id}", clienteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].clienteId").value(clienteId));
    }

    // ── POST /api/oportunidades ──────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("POST /api/oportunidades - Debería crear una oportunidad exitosamente")
    void create_WithValidDTO_ShouldReturnCreated() throws Exception {
        OpportunityRequestDTO requestDTO = crearRequestDTOValido();
        Opportunity creada = crearOpportunityMock(1L, 1L, 2L);
        OpportunityResponseDTO responseDTO = crearResponseDTOMock(1L, 1L, 2L);

        when(oportunidadacaseUse.createOpportunities(any(OpportunityRequestDTO.class))).thenReturn(creada);
        when(mapper.toDTO(creada)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/oportunidades")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idOportunidad").value(1L))
                .andExpect(jsonPath("$.clienteId").value(1L));
    }

    @Test
    @WithMockUser(roles = "ASESOR")
    @DisplayName("POST /api/oportunidades - Debería retornar 400 si el DTO no es válido")
    void create_WithInvalidDTO_ShouldReturnBadRequest() throws Exception {
        // clienteId y asesorId nulos, valorEstimado negativo, fecha pasada → viola @NotNull y @Future
        OpportunityRequestDTO invalidRequest = new OpportunityRequestDTO(
                null,
                null,
                "x".repeat(501),          // supera @Size(max=500)
                new BigDecimal("-1"),      // viola @DecimalMin
                LocalDate.now().minusDays(1) // viola @Future
        );

        mockMvc.perform(post("/api/oportunidades")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(oportunidadacaseUse, never()).createOpportunities(any());
    }

    // ── PUT /api/oportunidades/{id} ──────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ASESOR")
    @DisplayName("PUT /api/oportunidades/{id} - Debería actualizar una oportunidad correctamente")
    void update_WithValidDTO_ShouldReturnOk() throws Exception {
        Long id = 1L;
        OpportunityRequestDTO requestDTO = crearRequestDTOValido();
        Opportunity actualizada = crearOpportunityMock(id, 1L, 2L);
        OpportunityResponseDTO responseDTO = crearResponseDTOMock(id, 1L, 2L);

        when(oportunidadacaseUse.updateOpportunities(eq(id), any(OpportunityRequestDTO.class))).thenReturn(actualizada);
        when(mapper.toDTO(actualizada)).thenReturn(responseDTO);

        mockMvc.perform(put("/api/oportunidades/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idOportunidad").value(id));
    }

    // ── PATCH /api/oportunidades/{id}/etapa ─────────────────────────────────

    @Test
    @WithMockUser(roles = "GERENTE")
    @DisplayName("PATCH /api/oportunidades/{id}/etapa - Debería cambiar la etapa correctamente")
    void changeStage_ShouldReturnOk() throws Exception {
        Long id = 1L;
        StageOpportunity nuevaEtapa = StageOpportunity.PROPUESTA; // ajusta al valor real del enum
        Opportunity op = crearOpportunityMock(id, 1L, 2L);
        OpportunityResponseDTO dto = crearResponseDTOMock(id, 1L, 2L);

        when(oportunidadacaseUse.changeStage(id, nuevaEtapa)).thenReturn(op);
        when(mapper.toDTO(op)).thenReturn(dto);

        mockMvc.perform(patch("/api/oportunidades/{id}/etapa", id)
                        .with(csrf())
                        .param("etapa", nuevaEtapa.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idOportunidad").value(id));
    }

    // ── PATCH /api/oportunidades/{id}/probabilidad ──────────────────────────

    @Test
    @WithMockUser(roles = "ASESOR")
    @DisplayName("PATCH /api/oportunidades/{id}/probabilidad - Debería ajustar la probabilidad correctamente")
    void adjustProbability_ShouldReturnOk() throws Exception {
        Long id = 1L;
        int probabilidad = 80;
        Opportunity op = crearOpportunityMock(id, 1L, 2L);
        OpportunityResponseDTO dto = crearResponseDTOMock(id, 1L, 2L);

        when(oportunidadacaseUse.adjustProbability(id, probabilidad)).thenReturn(op);
        when(mapper.toDTO(op)).thenReturn(dto);

        mockMvc.perform(patch("/api/oportunidades/{id}/probabilidad", id)
                        .with(csrf())
                        .param("probabilidad", String.valueOf(probabilidad)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idOportunidad").value(id));
    }

    // ── PATCH /api/oportunidades/{id}/ganar ─────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("PATCH /api/oportunidades/{id}/ganar - Debería cerrar la oportunidad como ganada con 204")
    void closeAsWon_ShouldReturnNoContent() throws Exception {
        Long id = 1L;
        doNothing().when(oportunidadacaseUse).closeAsWon(id);

        mockMvc.perform(patch("/api/oportunidades/{id}/ganar", id)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(oportunidadacaseUse).closeAsWon(id);
    }

    // ── PATCH /api/oportunidades/{id}/perder ────────────────────────────────

    @Test
    @WithMockUser(roles = "GERENTE")
    @DisplayName("PATCH /api/oportunidades/{id}/perder - Debería cerrar la oportunidad como perdida con 204")
    void closeAsLost_ShouldReturnNoContent() throws Exception {
        Long id = 1L;
        doNothing().when(oportunidadacaseUse).closeAsLost(id);

        mockMvc.perform(patch("/api/oportunidades/{id}/perder", id)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(oportunidadacaseUse).closeAsLost(id);
    }
}