package com.terreneitors.backendclintec.dashboard.infrastructure.web;

import com.terreneitors.backendclintec.dashboard.application.DashboardService;
import com.terreneitors.backendclintec.dashboard.infrastructure.dto.AdminDashboardDTO;
import com.terreneitors.backendclintec.dashboard.infrastructure.dto.AsesorDashboardDTO;
import com.terreneitors.backendclintec.dashboard.infrastructure.dto.GerenteDashboardDTO;
import com.terreneitors.backendclintec.security.JwtFilter;
import com.terreneitors.backendclintec.security.JwtService;
import com.terreneitors.backendclintec.users.application.port.out.UserRepositoryPort;
import com.terreneitors.backendclintec.users.domain.User; // Asegúrate de importar tu entidad o dominio User legítimo
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

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest(DashboardController.class)
@Import(DashboardControllerTest.TestSecurityConfig.class)
class DashboardControllerTest {

    @EnableWebSecurity
    @EnableMethodSecurity
    @Configuration
    static class TestSecurityConfig {
        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) {
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
    private DashboardService dashboardService;

    @MockitoBean
    private UserRepositoryPort usuarioRepo;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtFilter jwtFilter;

    private AdminDashboardDTO adminDTO;
    private GerenteDashboardDTO gerenteDTO;
    private AsesorDashboardDTO asesorDTO;

    @BeforeEach
    void setUp() {
        adminDTO = new AdminDashboardDTO(
                50L, 10L, 30L, 5L, 15L, 20L, 5L, BigDecimal.valueOf(150000.0),
                4L, 3L, 5L, 3L, 20L, 5L
        );

        gerenteDTO = new GerenteDashboardDTO(
                100L, 60L, 25L, 40L, 10L, BigDecimal.valueOf(300000.0),
                5L, 5L, 10L, 5L, 40L, 10L
        );

        asesorDTO = new AsesorDashboardDTO(
                12L, 6L, 4L, 2L, BigDecimal.valueOf(45000.0), 3L,
                2L, 1L, 2L, 1L, 4L, 2L
        );
    }

    @Test
    @DisplayName("Debe permitir acceso al Dashboard de Admin a usuarios con rol ADMINISTRADOR")
    void debeRetornarDashboardAdmin() throws Exception {
        when(dashboardService.getAdminDashboard()).thenReturn(adminDTO);

        mockMvc.perform(get("/api/dashboard/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("admin@clintec.com").roles("ADMINISTRADOR")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe permitir acceso al Dashboard de Gerente a usuarios con rol GERENTE")
    void debeRetornarDashboardGerente() throws Exception {
        when(dashboardService.getGerenteDashboard()).thenReturn(gerenteDTO);

        mockMvc.perform(get("/api/dashboard/gerente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("gerente@clintec.com").roles("GERENTE")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe permitir acceso al Dashboard de Asesor resolviendo su ID por email")
    void debeRetornarDashboardAsesor() throws Exception {
        String emailAsesor = "asesor@clintec.com";
        Long asesorId = 2L;

        // Mockear el usuario devuelto por el puerto/repositorio
        User mockUser = Mockito.mock(User.class);
        when(mockUser.getId()).thenReturn(asesorId);

        when(usuarioRepo.findByEmail(emailAsesor)).thenReturn(Optional.of(mockUser));
        when(dashboardService.getAsesorDashboard(asesorId)).thenReturn(asesorDTO);

        mockMvc.perform(get("/api/dashboard/asesor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(emailAsesor).roles("ASESOR")))
                .andExpect(status().isOk());
    }

}