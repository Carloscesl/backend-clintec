package com.terreneitors.backendclintec.users.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.terreneitors.backendclintec.users.application.port.in.UserCrudUseCase;
import com.terreneitors.backendclintec.users.domain.Rol;
import com.terreneitors.backendclintec.users.domain.User;
import com.terreneitors.backendclintec.users.infrastructure.dto.UserRequestDTO;
import com.terreneitors.backendclintec.users.infrastructure.dto.UserResponseDTO;
import com.terreneitors.backendclintec.users.infrastructure.persistence.mapper.UserPersistenceMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UserCrudUseCase userCrudUseCase;

    @MockitoBean
    private UserPersistenceMapper mapper;

    // Mocks de seguridad para evitar errores de contexto
    @MockitoBean
    private com.terreneitors.backendclintec.security.JwtService jwtService;
    @MockitoBean
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    private UserResponseDTO crearResponse(Long id, String email, Rol rol) {
        return new UserResponseDTO(id, "Nombre", email, rol, true);
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("GET /api/usuarios - Listar todos los usuarios")
    void list_ShouldReturnUsers() throws Exception {
        User user = new User();
        UserResponseDTO dto = crearResponse(1L, "admin@test.com", Rol.ADMINISTRADOR);

        when(userCrudUseCase.findAll()).thenReturn(List.of(user));
        when(mapper.toDTO(user)).thenReturn(dto);

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("admin@test.com"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("POST /api/usuarios - Crear usuario exitosamente")
    void create_ShouldReturnCreated() throws Exception {
        // CORRECCIÓN: "pass123" tiene 7 caracteres, cumpliendo la regla de >= 6
        UserRequestDTO req = new UserRequestDTO("Name", "test@test.com", "pass123", Rol.ADMINISTRADOR);

        User user = new User();
        UserResponseDTO res = crearResponse(1L, "test@test.com", Rol.ADMINISTRADOR);

        when(userCrudUseCase.createUser(any())).thenReturn(user);
        when(mapper.toDTO(user)).thenReturn(res);

        mockMvc.perform(post("/api/usuarios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated()); // Ahora sí pasará
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("PATCH /api/usuarios/{email}/desactivar - Desactivar usuario")
    void desactivate_ShouldReturnNoContent() throws Exception {
        String email = "test@test.com";
        doNothing().when(userCrudUseCase).desactivateUser(email);

        mockMvc.perform(patch("/api/usuarios/{email}/desactivar", email)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("GET /api/usuarios/total - Contar usuarios")
    void count_ShouldReturnTotal() throws Exception {
        when(userCrudUseCase.count()).thenReturn(10L);

        mockMvc.perform(get("/api/usuarios/total"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    @DisplayName("GET /api/usuarios/filtrar - Filtrar por rol")
    void listByRole_ShouldReturnUsers() throws Exception {
        User user = new User();
        UserResponseDTO dto = crearResponse(1L, "asesor@test.com", Rol.ASESOR);

        when(userCrudUseCase.findAllRol(Rol.ASESOR)).thenReturn(List.of(user));
        when(mapper.toDTO(user)).thenReturn(dto);

        mockMvc.perform(get("/api/usuarios/filtrar")
                        .param("rol", "ASESOR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rol").value("ASESOR"));
    }
}