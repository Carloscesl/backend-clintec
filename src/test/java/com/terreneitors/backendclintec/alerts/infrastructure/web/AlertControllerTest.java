package com.terreneitors.backendclintec.alerts.infrastructure.web;

import com.terreneitors.backendclintec.alerts.application.port.in.AlertCrudUseCase;
import com.terreneitors.backendclintec.alerts.domain.Alert;
import com.terreneitors.backendclintec.alerts.domain.StateAlert;
import com.terreneitors.backendclintec.alerts.domain.TypeAlert;
import com.terreneitors.backendclintec.alerts.infrastructure.dto.AlertResponseDTO;
import com.terreneitors.backendclintec.alerts.infrastructure.persistence.mapper.AlertPersistenceMapper;
import com.terreneitors.backendclintec.security.JwtFilter;
import com.terreneitors.backendclintec.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest(AlertController.class)
@Import(AlertControllerTest.TestSecurityConfig.class)
class AlertControllerTest {

    @EnableWebSecurity
    @EnableMethodSecurity
    @Configuration
    static class TestSecurityConfig {

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http){
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth
                            .anyRequest().authenticated()
                    );
            return http.build();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AlertCrudUseCase useCase;

    @MockitoBean
    private AlertPersistenceMapper mapper;

    @MockitoBean
    private JwtService jwtService;

    // JwtFilter mockeado como bean para satisfacer dependencias,
    // pero NO se registra en el SecurityFilterChain del test
    @MockitoBean
    private JwtFilter jwtFilter;

    private Alert alert;
    private AlertResponseDTO dto;

    @BeforeEach
    void setUp() {

        alert = Mockito.mock(Alert.class);

        dto = new AlertResponseDTO(
                1L,
                10L,
                2L,
                "Alerta pendiente",
                TypeAlert.OPORTUNIDAD,
                StateAlert.PENDIENTE,
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    // Usamos user() de SecurityMockMvcRequestPostProcessors en lugar de @WithMockUser
    // para evitar conflictos con el JwtFilter mockeado en la cadena de seguridad.
    // roles() agrega automáticamente el prefijo ROLE_, igual que @WithMockUser.

    @Test
    @DisplayName("Debe listar alertas")
    void debeListarAlertas() throws Exception {

        when(useCase.findAll()).thenReturn(List.of(alert));
        when(mapper.toDTO(alert)).thenReturn(dto);

        mockMvc.perform(get("/api/alertas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("admin").roles("ADMINISTRADOR")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe buscar alerta por id")
    void debeBuscarPorId() throws Exception {

        when(useCase.buscarPorId(1L)).thenReturn(Optional.of(alert));
        when(mapper.toDTO(alert)).thenReturn(dto);

        mockMvc.perform(get("/api/alertas/id/1")
                        .with(user("asesor").roles("ASESOR")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe listar alertas pendientes")
    void debeListarPendientes() throws Exception {

        when(useCase.buscarPendientes()).thenReturn(List.of(alert));
        when(mapper.toDTO(alert)).thenReturn(dto);

        mockMvc.perform(get("/api/alertas/pendientes")
                        .with(user("gerente").roles("GERENTE")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe listar alertas por cliente")
    void debeListarPorCliente() throws Exception {

        when(useCase.buscarPorCliente(10L)).thenReturn(List.of(alert));
        when(mapper.toDTO(alert)).thenReturn(dto);

        mockMvc.perform(get("/api/alertas/cliente/10")
                        .with(user("asesor").roles("ASESOR")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe listar alertas por usuario")
    void debeListarPorUsuario() throws Exception {

        when(useCase.buscarPorUsuario(2L)).thenReturn(List.of(alert));
        when(mapper.toDTO(alert)).thenReturn(dto);

        mockMvc.perform(get("/api/alertas/usuario/2")
                        .with(user("asesor").roles("ASESOR")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe marcar alerta como vista")
    void debeMarcarComoVista() throws Exception {

        when(useCase.marcarComoVista(anyLong())).thenReturn(alert);
        when(mapper.toDTO(alert)).thenReturn(dto);

        mockMvc.perform(patch("/api/alertas/1/vista")
                        .with(user("asesor").roles("ASESOR")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe resolver alerta")
    void debeResolverAlerta() throws Exception {

        when(useCase.resolver(anyLong())).thenReturn(alert);
        when(mapper.toDTO(alert)).thenReturn(dto);

        mockMvc.perform(patch("/api/alertas/1/resolver")
                        .with(user("asesor").roles("ASESOR")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar 403 sin autenticación")
    void debeRetornar403SinAutenticacion() throws Exception {
        // Sin .with(user(...)) → Spring Security bloquea con 403
        mockMvc.perform(get("/api/alertas"))
                .andExpect(status().isForbidden());
    }
}