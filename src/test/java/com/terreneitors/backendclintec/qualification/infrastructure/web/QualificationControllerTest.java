package com.terreneitors.backendclintec.qualification.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.terreneitors.backendclintec.qualification.application.port.in.QualificationCrudUseCase;
import com.terreneitors.backendclintec.qualification.application.port.in.QualificationQueryUseCase;
import com.terreneitors.backendclintec.qualification.domain.Qualification;
import com.terreneitors.backendclintec.qualification.domain.QualificationClient;
import com.terreneitors.backendclintec.qualification.domain.QualificationHistory;
import com.terreneitors.backendclintec.qualification.infrastructure.dto.QualificationHistoryResponseDTO;
import com.terreneitors.backendclintec.qualification.infrastructure.dto.QualificationRequestDTO;
import com.terreneitors.backendclintec.qualification.infrastructure.dto.QualificationResponseDTO;
import com.terreneitors.backendclintec.qualification.infrastructure.persistence.mapper.QualificationHistoryPersistenceMapper;
import com.terreneitors.backendclintec.qualification.infrastructure.persistence.mapper.QualificationPersistenceMapper;
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
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QualificationController.class)
class QualificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @MockitoBean
    private QualificationCrudUseCase useCase;

    @MockitoBean
    private QualificationQueryUseCase queryUseCase;

    @MockitoBean
    private QualificationPersistenceMapper mapper;

    @MockitoBean
    private QualificationHistoryPersistenceMapper historyMapper;

    @MockitoBean
    private com.terreneitors.backendclintec.security.JwtService jwtService;

    @MockitoBean
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    // ── Helpers ─────────────────────────────────────────────────────────────

    private QualificationClient crearClienteMock(Long id, Long clienteId, int puntaje, Qualification clasificacion) {
        QualificationClient c = new QualificationClient();
        c.setId(id);
        c.setClienteId(clienteId);
        c.setPuntaje(puntaje);
        c.setClasificacion(clasificacion);
        c.setUltimaActualizacion(LocalDateTime.now());
        return c;
    }

    private QualificationHistory crearHistoryMock(Long id, Long clienteId) {
        QualificationHistory h = new QualificationHistory();
        h.setId(id);
        h.setClienteId(clienteId);
        h.setPuntajeAnterior(60);
        h.setPuntajeNuevo(80);
        h.setMotivo("Actualización manual");
        h.setFecha(LocalDateTime.now());
        return h;
    }

    private QualificationResponseDTO crearResponseDTOMock(Long id, Long clienteId,
                                                          int puntaje, Qualification clasificacion) {
        return new QualificationResponseDTO(id, clienteId, puntaje, clasificacion, LocalDateTime.now());
    }

    private QualificationHistoryResponseDTO crearHistoryDTOMock(Long id, Long clienteId) {
        return new QualificationHistoryResponseDTO(id, clienteId, 60, 80, "Actualización manual", LocalDateTime.now());
    }

    // ── GET /api/calificaciones ──────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("GET /api/calificaciones - Debería retornar la lista de calificaciones con 200 OK")
    void listar_ShouldReturnQualificationsList() throws Exception {
        QualificationClient c1 = crearClienteMock(1L, 10L, 85, Qualification.CALIENTE);
        QualificationClient c2 = crearClienteMock(2L, 20L, 40, Qualification.TIBIO);
        QualificationResponseDTO dto1 = crearResponseDTOMock(1L, 10L, 85, Qualification.CALIENTE);
        QualificationResponseDTO dto2 = crearResponseDTOMock(2L, 20L, 40, Qualification.TIBIO);

        when(useCase.findAll()).thenReturn(List.of(c1, c2));
        when(mapper.toDTO(c1)).thenReturn(dto1);
        when(mapper.toDTO(c2)).thenReturn(dto2);

        mockMvc.perform(get("/api/calificaciones"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].clasificacion").value("CALIENTE"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].clasificacion").value("TIBIO"));
    }

    // ── GET /api/calificaciones/cliente/{clienteId} ──────────────────────────

    @Test
    @WithMockUser(roles = "ASESOR")
    @DisplayName("GET /api/calificaciones/cliente/{clienteId} - Debería retornar la calificación si existe")
    void buscarPorCliente_WhenExists_ShouldReturnQualification() throws Exception {
        Long clienteId = 10L;
        QualificationClient client = crearClienteMock(1L, clienteId, 90, Qualification.VIP);
        QualificationResponseDTO dto = crearResponseDTOMock(1L, clienteId, 90, Qualification.VIP);

        when(useCase.findByClientId(clienteId)).thenReturn(Optional.of(client));
        when(mapper.toDTO(client)).thenReturn(dto);

        mockMvc.perform(get("/api/calificaciones/cliente/{clienteId}", clienteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value(clienteId))
                .andExpect(jsonPath("$.clasificacion").value("VIP"))
                .andExpect(jsonPath("$.puntaje").value(90));
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    @DisplayName("GET /api/calificaciones/cliente/{clienteId} - Debería retornar 404 si no existe")
    void buscarPorCliente_WhenNotExists_ShouldReturnNotFound() throws Exception {
        Long clienteId = 99L;
        when(useCase.findByClientId(clienteId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/calificaciones/cliente/{clienteId}", clienteId))
                .andExpect(status().isNotFound());
    }

    // ── PATCH /api/calificaciones/cliente/{clienteId}/puntaje ───────────────

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("PATCH /api/calificaciones/cliente/{clienteId}/puntaje - Debería actualizar el puntaje correctamente")
    void actualizarPuntaje_WithValidDTO_ShouldReturnOk() throws Exception {
        Long clienteId = 10L;
        QualificationRequestDTO requestDTO = new QualificationRequestDTO(75);
        QualificationClient client = crearClienteMock(1L, clienteId, 75, Qualification.CALIENTE);
        QualificationResponseDTO responseDTO = crearResponseDTOMock(1L, clienteId, 75, Qualification.CALIENTE);

        when(useCase.updateScore(clienteId, 75)).thenReturn(client);
        when(mapper.toDTO(client)).thenReturn(responseDTO);

        mockMvc.perform(patch("/api/calificaciones/cliente/{clienteId}/puntaje", clienteId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.puntaje").value(75))
                .andExpect(jsonPath("$.clasificacion").value("CALIENTE"));
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    @DisplayName("PATCH /api/calificaciones/cliente/{clienteId}/puntaje - Debería retornar 400 si el puntaje supera 100")
    void actualizarPuntaje_WithScoreAboveMax_ShouldReturnBadRequest() throws Exception {
        QualificationRequestDTO invalidRequest = new QualificationRequestDTO(150); // viola @Max(100)

        mockMvc.perform(patch("/api/calificaciones/cliente/{clienteId}/puntaje", 10L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(useCase, never()).updateScore(anyLong(), anyInt());
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    @DisplayName("PATCH /api/calificaciones/cliente/{clienteId}/puntaje - Debería retornar 400 si el puntaje es negativo")
    void actualizarPuntaje_WithNegativeScore_ShouldReturnBadRequest() throws Exception {
        QualificationRequestDTO invalidRequest = new QualificationRequestDTO(-1); // viola @Min(0)

        mockMvc.perform(patch("/api/calificaciones/cliente/{clienteId}/puntaje", 10L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(useCase, never()).updateScore(anyLong(), anyInt());
    }

    // ── GET /api/calificaciones/clasificacion/{nivel} ────────────────────────

    @Test
    @WithMockUser(roles = "GERENTE")
    @DisplayName("GET /api/calificaciones/clasificacion/{nivel} - Debería retornar clientes VIP")
    void porClasificacion_VIP_ShouldReturnList() throws Exception {
        Qualification nivel = Qualification.VIP;
        QualificationClient client = crearClienteMock(1L, 10L, 95, nivel);
        QualificationResponseDTO dto = crearResponseDTOMock(1L, 10L, 95, nivel);

        when(queryUseCase.findByClasificacion(nivel)).thenReturn(List.of(client));
        when(mapper.toDTO(client)).thenReturn(dto);

        mockMvc.perform(get("/api/calificaciones/clasificacion/{nivel}", nivel.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].clasificacion").value("VIP"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("GET /api/calificaciones/clasificacion/{nivel} - Debería retornar clientes FRIO")
    void porClasificacion_FRIO_ShouldReturnList() throws Exception {
        Qualification nivel = Qualification.FRIO;
        QualificationClient client = crearClienteMock(2L, 20L, 10, nivel);
        QualificationResponseDTO dto = crearResponseDTOMock(2L, 20L, 10, nivel);

        when(queryUseCase.findByClasificacion(nivel)).thenReturn(List.of(client));
        when(mapper.toDTO(client)).thenReturn(dto);

        mockMvc.perform(get("/api/calificaciones/clasificacion/{nivel}", nivel.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clasificacion").value("FRIO"));
    }

    // ── GET /api/calificaciones/top/{n} ─────────────────────────────────────

    @Test
    @WithMockUser(roles = "ASESOR")
    @DisplayName("GET /api/calificaciones/top/{n} - Debería retornar el top N de calificaciones")
    void topN_ShouldReturnTopList() throws Exception {
        int n = 2;
        QualificationClient c1 = crearClienteMock(1L, 10L, 95, Qualification.VIP);
        QualificationClient c2 = crearClienteMock(2L, 20L, 88, Qualification.CALIENTE);
        QualificationResponseDTO dto1 = crearResponseDTOMock(1L, 10L, 95, Qualification.VIP);
        QualificationResponseDTO dto2 = crearResponseDTOMock(2L, 20L, 88, Qualification.CALIENTE);

        when(queryUseCase.findTopN(n)).thenReturn(List.of(c1, c2));
        when(mapper.toDTO(c1)).thenReturn(dto1);
        when(mapper.toDTO(c2)).thenReturn(dto2);

        mockMvc.perform(get("/api/calificaciones/top/{n}", n))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].puntaje").value(95))
                .andExpect(jsonPath("$[1].puntaje").value(88));
    }

    // ── GET /api/calificaciones/en-riesgo ────────────────────────────────────

    @Test
    @WithMockUser(roles = "GERENTE")
    @DisplayName("GET /api/calificaciones/en-riesgo - Debería retornar clientes en riesgo")
    void enRiesgo_ShouldReturnList() throws Exception {
        QualificationClient client = crearClienteMock(1L, 10L, 15, Qualification.FRIO);
        QualificationResponseDTO dto = crearResponseDTOMock(1L, 10L, 15, Qualification.FRIO);

        when(queryUseCase.findEnRiesgo()).thenReturn(List.of(client));
        when(mapper.toDTO(client)).thenReturn(dto);

        mockMvc.perform(get("/api/calificaciones/en-riesgo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].clasificacion").value("FRIO"))
                .andExpect(jsonPath("$[0].puntaje").value(15));
    }

    // ── GET /api/calificaciones/distribucion ─────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("GET /api/calificaciones/distribucion - Debería retornar la distribución por nivel")
    void distribucion_ShouldReturnDistribucionDTO() throws Exception {
        Map<Qualification, Long> mapa = Map.of(
                Qualification.FRIO,     5L,
                Qualification.TIBIO,    8L,
                Qualification.CALIENTE, 3L,
                Qualification.VIP,      2L
        );
        when(queryUseCase.distribucionPorNivel()).thenReturn(mapa);

        mockMvc.perform(get("/api/calificaciones/distribucion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.distribucion.FRIO").value(5))
                .andExpect(jsonPath("$.distribucion.TIBIO").value(8))
                .andExpect(jsonPath("$.distribucion.CALIENTE").value(3))
                .andExpect(jsonPath("$.distribucion.VIP").value(2));
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    @DisplayName("GET /api/calificaciones/distribucion - Debería retornar distribución vacía si no hay datos")
    void distribucion_WhenEmpty_ShouldReturnEmptyMap() throws Exception {
        when(queryUseCase.distribucionPorNivel()).thenReturn(Map.of());

        mockMvc.perform(get("/api/calificaciones/distribucion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.distribucion").isEmpty());
    }

    // ── GET /api/calificaciones/cliente/{clienteId}/historial ────────────────

    @Test
    @WithMockUser(roles = "ASESOR")
    @DisplayName("GET /api/calificaciones/cliente/{clienteId}/historial - Debería retornar el historial del cliente")
    void historial_ShouldReturnHistoryList() throws Exception {
        Long clienteId = 10L;
        QualificationHistory history = crearHistoryMock(1L, clienteId);
        QualificationHistoryResponseDTO hDto = crearHistoryDTOMock(1L, clienteId);

        when(queryUseCase.historialPorCliente(clienteId)).thenReturn(List.of(history));
        when(historyMapper.toHistoryDTO(history)).thenReturn(hDto);

        mockMvc.perform(get("/api/calificaciones/cliente/{clienteId}/historial", clienteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].clienteId").value(clienteId))
                .andExpect(jsonPath("$[0].puntajeAnterior").value(60))
                .andExpect(jsonPath("$[0].puntajeNuevo").value(80))
                .andExpect(jsonPath("$[0].motivo").value("Actualización manual"));
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    @DisplayName("GET /api/calificaciones/cliente/{clienteId}/historial - Debería retornar lista vacía si no hay historial")
    void historial_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        Long clienteId = 99L;
        when(queryUseCase.historialPorCliente(clienteId)).thenReturn(List.of());

        mockMvc.perform(get("/api/calificaciones/cliente/{clienteId}/historial", clienteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }
}