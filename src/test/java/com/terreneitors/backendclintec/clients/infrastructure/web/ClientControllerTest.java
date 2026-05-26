package com.terreneitors.backendclintec.clients.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.terreneitors.backendclintec.clients.application.port.in.ClientCrudUseCase;
import com.terreneitors.backendclintec.clients.domain.Client;
import com.terreneitors.backendclintec.clients.infrastructure.dto.ClientRequestDTO;
import com.terreneitors.backendclintec.clients.infrastructure.dto.ClientResponseDTO;
import com.terreneitors.backendclintec.clients.infrastructure.persistence.mapper.ClientPersistenceMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean; // <- Importación correcta para Spring Boot 3.4+
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

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean // <- Solución al error usando la nueva especificación
    private ClientCrudUseCase caseUse;

    @MockitoBean // <- Solución al error usando la nueva especificación
    private ClientPersistenceMapper mapper;

    @MockitoBean
    private com.terreneitors.backendclintec.security.JwtService jwtService;

    @MockitoBean
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    // Helper corregido y cerrado correctamente para construir clientes de dominio
    private Client crearClienteMock(Long id, String nombre, String email) {
        Client client = new Client();
        client.setId(id);
        client.setNombreCliente(nombre);
        client.setEmail(email);
        return client;
    }

    // Helper corregido y cerrado correctamente para construir DTOs de respuesta
    private ClientResponseDTO crearResponseDTOMock(Long id, String nombre, String email) {
        return new ClientResponseDTO(
                id,
                nombre,
                "Terreneitors S.A.S.",
                email,
                "3001234567",
                "Calle 123",
                LocalDateTime.now()
        );
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("GET /api/clientes - Debería retornar la lista de clientes con estado 200 OK")
    void list_ShouldReturnClientsList() throws Exception {
        // Arrange
        Client c1 = crearClienteMock(1L, "Carlos", "carlos@test.com");
        Client c2 = crearClienteMock(2L, "Fabiana", "fabiana@test.com");
        ClientResponseDTO dto1 = crearResponseDTOMock(1L, "Carlos", "carlos@test.com");
        ClientResponseDTO dto2 = crearResponseDTOMock(2L, "Fabiana", "fabiana@test.com");

        when(caseUse.findAll()).thenReturn(List.of(c1, c2));
        when(mapper.toDTO(c1)).thenReturn(dto1);
        when(mapper.toDTO(c2)).thenReturn(dto2);

        // Act & Assert
        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nombreCliente").value("Carlos"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].nombreCliente").value("Fabiana"));
    }

    @Test
    @WithMockUser(roles = "ASESOR")
    @DisplayName("GET /api/clientes/{id} - Debería retornar un cliente si existe")
    void findById_WhenClientExists_ShouldReturnClient() throws Exception {
        // Arrange
        Long id = 1L;
        Client client = crearClienteMock(id, "Carlos", "carlos@test.com");
        ClientResponseDTO dto = crearResponseDTOMock(id, "Carlos", "carlos@test.com");

        when(caseUse.findById(id)).thenReturn(Optional.of(client));
        when(mapper.toDTO(client)).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(get("/api/clientes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.email").value("carlos@test.com"));
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    @DisplayName("GET /api/clientes/{id} - Debería retornar 404 si el cliente no existe")
    void findById_WhenClientDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Arrange
        Long id = 99L;
        when(caseUse.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/clientes/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("GET /api/clientes/email/{email} - Debería retornar un cliente por su email")
    void findByEmail_WhenClientExists_ShouldReturnClient() throws Exception {
        // Arrange
        String email = "carlos@test.com";
        Client client = crearClienteMock(1L, "Carlos", email);
        ClientResponseDTO dto = crearResponseDTOMock(1L, "Carlos", email);

        when(caseUse.findByEmail(email)).thenReturn(Optional.of(client));
        when(mapper.toDTO(client)).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(get("/api/clientes/email/{email}", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("POST /api/clientes - Debería crear un cliente exitosamente")
    void create_WithValidDTO_ShouldReturnCreated() throws Exception {
        // Arrange
        ClientRequestDTO requestDTO = new ClientRequestDTO("Carlos", "Terreneitors S.A.S.", "carlos@test.com", "3001234567", "Calle 123");
        Client creado = crearClienteMock(1L, "Carlos", "carlos@test.com");
        ClientResponseDTO responseDTO = crearResponseDTOMock(1L, "Carlos", "carlos@test.com");

        when(caseUse.createClient(any(ClientRequestDTO.class))).thenReturn(creado);
        when(mapper.toDTO(creado)).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/clientes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombreCliente").value("Carlos"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("POST /api/clientes - Debería retornar 400 Bad Request si el DTO no es válido")
    void create_WithInvalidDTO_ShouldReturnBadRequest() throws Exception {
        // Arrange
        ClientRequestDTO invalidRequest = new ClientRequestDTO("", "", "emailInvalido", "123", "");

        // Act & Assert
        mockMvc.perform(post("/api/clientes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(caseUse, never()).createClient(any());
    }

    @Test
    @WithMockUser(roles = "ASESOR")
    @DisplayName("PUT /api/clientes/{id} - Debería actualizar un cliente correctamente")
    void update_WithValidDTO_ShouldReturnOk() throws Exception {
        // Arrange
        Long id = 1L;
        ClientRequestDTO requestDTO = new ClientRequestDTO("Carlos Editado", "Terreneitors S.A.S.", "carlos@test.com", "3001234567", "Calle 123");
        Client actualizado = crearClienteMock(id, "Carlos Editado", "carlos@test.com");
        ClientResponseDTO responseDTO = crearResponseDTOMock(id, "Carlos Editado", "carlos@test.com");

        when(caseUse.updateClient(eq(id), any(ClientRequestDTO.class))).thenReturn(actualizado);
        when(mapper.toDTO(actualizado)).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(put("/api/clientes/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreCliente").value("Carlos Editado"));
    }
}