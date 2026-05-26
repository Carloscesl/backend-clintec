package com.terreneitors.backendclintec.sales.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.terreneitors.backendclintec.sales.application.port.in.SaleCrudUseCase;
import com.terreneitors.backendclintec.sales.domain.PaymentMethod;
import com.terreneitors.backendclintec.sales.domain.Sale;
import com.terreneitors.backendclintec.sales.infrastructure.dto.SaleRequestDTO;
import com.terreneitors.backendclintec.sales.infrastructure.dto.SaleResponseDTO;
import com.terreneitors.backendclintec.sales.infrastructure.persistence.Mapper.SalePersistenceMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SaleController.class)
class SaleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @MockitoBean
    private SaleCrudUseCase ventaCasosUso;

    @MockitoBean
    private SalePersistenceMapper ventaMapper;

    @MockitoBean
    private com.terreneitors.backendclintec.security.JwtService jwtService;

    @MockitoBean
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    // ── Helpers ─────────────────────────────────────────────────────────────

    private Sale crearSaleMock(Long idVenta, Long idOportunidad, Long idAsesor) {
        Sale s = new Sale();
        s.setIdVenta(idVenta);
        s.setIdOportunidad(idOportunidad);
        s.setIdAsesor(idAsesor);
        s.setValorVenta(new BigDecimal("1500000"));
        s.setMetodoPago(PaymentMethod.TRANSFERENCIA);
        s.setNotas("Nota de prueba");
        s.setFechaVenta(LocalDateTime.now());
        s.setFechaActualizacion(LocalDateTime.now());
        return s;
    }

    private SaleResponseDTO crearResponseDTOMock(Long idVenta, Long idOportunidad, Long idAsesor) {
        return new SaleResponseDTO(
                idVenta,
                idOportunidad,
                idAsesor,
                new BigDecimal("1500000"),
                PaymentMethod.TRANSFERENCIA,
                "Nota de prueba",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private SaleRequestDTO crearRequestDTOValido() {
        return new SaleRequestDTO(
                1L,
                2L,
                new BigDecimal("1500000"),
                "Nota de prueba",
                PaymentMethod.TRANSFERENCIA
        );
    }

    // ── GET /api/venta ───────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("GET /api/venta - Debería retornar la lista de ventas con estado 200 OK")
    void listAll_ShouldReturnSalesList() throws Exception {
        Sale s1 = crearSaleMock(1L, 10L, 2L);
        Sale s2 = crearSaleMock(2L, 20L, 3L);
        SaleResponseDTO dto1 = crearResponseDTOMock(1L, 10L, 2L);
        SaleResponseDTO dto2 = crearResponseDTOMock(2L, 20L, 3L);

        when(ventaCasosUso.findAll()).thenReturn(List.of(s1, s2));
        when(ventaMapper.toDTO(s1)).thenReturn(dto1);
        when(ventaMapper.toDTO(s2)).thenReturn(dto2);

        mockMvc.perform(get("/api/venta"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].idVenta").value(1L))
                .andExpect(jsonPath("$[0].paymentMethod").value("TRANSFERENCIA"))
                .andExpect(jsonPath("$[1].idVenta").value(2L));
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    @DisplayName("GET /api/venta - Debería retornar lista vacía si no hay ventas")
    void listAll_WhenNoSales_ShouldReturnEmptyList() throws Exception {
        when(ventaCasosUso.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/venta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    // ── GET /api/venta/{id} ──────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "GERENTE")
    @DisplayName("GET /api/venta/{id} - Debería retornar una venta si existe")
    void findId_WhenExists_ShouldReturnSale() throws Exception {
        Long id = 1L;
        Sale sale = crearSaleMock(id, 10L, 2L);
        SaleResponseDTO dto = crearResponseDTOMock(id, 10L, 2L);

        when(ventaCasosUso.findId(id)).thenReturn(Optional.of(sale));
        when(ventaMapper.toDTO(sale)).thenReturn(dto);

        mockMvc.perform(get("/api/venta/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idVenta").value(id))
                .andExpect(jsonPath("$.idOportunidad").value(10L))
                .andExpect(jsonPath("$.idAsesor").value(2L))
                .andExpect(jsonPath("$.paymentMethod").value("TRANSFERENCIA"));
    }

    @Test
    @WithMockUser(roles = "ASESOR")
    @DisplayName("GET /api/venta/{id} - Debería retornar 404 si la venta no existe")
    void findId_WhenNotExists_ShouldReturnNotFound() throws Exception {
        Long id = 99L;
        when(ventaCasosUso.findId(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/venta/{id}", id))
                .andExpect(status().isNotFound());
    }

    // ── GET /api/venta/buscarasesor/{id} ────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("GET /api/venta/buscarasesor/{id} - Debería retornar las ventas de un asesor")
    void findAssessor_ShouldReturnList() throws Exception {
        Long asesorId = 2L;
        Sale sale = crearSaleMock(1L, 10L, asesorId);
        SaleResponseDTO dto = crearResponseDTOMock(1L, 10L, asesorId);

        when(ventaCasosUso.findIdAssessor(asesorId)).thenReturn(List.of(sale));
        when(ventaMapper.toDTO(sale)).thenReturn(dto);

        mockMvc.perform(get("/api/venta/buscarasesor/{id}", asesorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].idAsesor").value(asesorId));
    }

    @Test
    @WithMockUser(roles = "ASESOR")
    @DisplayName("GET /api/venta/buscarasesor/{id} - Debería retornar lista vacía si el asesor no tiene ventas")
    void findAssessor_WhenNoSales_ShouldReturnEmptyList() throws Exception {
        Long asesorId = 99L;
        when(ventaCasosUso.findIdAssessor(asesorId)).thenReturn(List.of());

        mockMvc.perform(get("/api/venta/buscarasesor/{id}", asesorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    // ── GET /api/venta/buscaroportunidad/{id} ───────────────────────────────

    @Test
    @WithMockUser(roles = "GERENTE")
    @DisplayName("GET /api/venta/buscaroportunidad/{id} - Debería retornar las ventas de una oportunidad")
    void findOpportunity_ShouldReturnList() throws Exception {
        Long oportunidadId = 10L;
        Sale sale = crearSaleMock(1L, oportunidadId, 2L);
        SaleResponseDTO dto = crearResponseDTOMock(1L, oportunidadId, 2L);

        when(ventaCasosUso.findIdOpportunity(oportunidadId)).thenReturn(List.of(sale));
        when(ventaMapper.toDTO(sale)).thenReturn(dto);

        mockMvc.perform(get("/api/venta/buscaroportunidad/{id}", oportunidadId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].idOportunidad").value(oportunidadId));
    }

    @Test
    @WithMockUser(roles = "ASESOR")
    @DisplayName("GET /api/venta/buscaroportunidad/{id} - Debería retornar lista vacía si la oportunidad no tiene ventas")
    void findOpportunity_WhenNoSales_ShouldReturnEmptyList() throws Exception {
        Long oportunidadId = 99L;
        when(ventaCasosUso.findIdOpportunity(oportunidadId)).thenReturn(List.of());

        mockMvc.perform(get("/api/venta/buscaroportunidad/{id}", oportunidadId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    // ── POST /api/venta ──────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("POST /api/venta - Debería crear una venta exitosamente con estado 201")
    void createSale_WithValidDTO_ShouldReturnCreated() throws Exception {
        SaleRequestDTO requestDTO = crearRequestDTOValido();
        Sale creada = crearSaleMock(1L, 1L, 2L);
        SaleResponseDTO responseDTO = crearResponseDTOMock(1L, 1L, 2L);

        when(ventaCasosUso.createSale(any(SaleRequestDTO.class))).thenReturn(creada);
        when(ventaMapper.toDTO(creada)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/venta")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idVenta").value(1L))
                .andExpect(jsonPath("$.paymentMethod").value("TRANSFERENCIA"))
                .andExpect(jsonPath("$.valorVenta").value(1500000));
    }

    @Test
    @WithMockUser(roles = "ASESOR")
    @DisplayName("POST /api/venta - Debería retornar 400 si los campos obligatorios son nulos")
    void createSale_WithNullFields_ShouldReturnBadRequest() throws Exception {
        SaleRequestDTO invalidRequest = new SaleRequestDTO(null, null, null, null, null);

        mockMvc.perform(post("/api/venta")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(ventaCasosUso, never()).createSale(any());
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    @DisplayName("POST /api/venta - Debería retornar 400 si el valorVenta es 0")
    void createSale_WithZeroValorVenta_ShouldReturnBadRequest() throws Exception {
        // BigDecimal.ZERO viola @DecimalMin(value = "0.0", inclusive = false)
        SaleRequestDTO invalidRequest = new SaleRequestDTO(
                1L, 2L, BigDecimal.ZERO, "Nota", PaymentMethod.EFECTIVO
        );

        mockMvc.perform(post("/api/venta")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(ventaCasosUso, never()).createSale(any());
    }

    @Test
    @WithMockUser(roles = "ASESOR")
    @DisplayName("POST /api/venta - Debería retornar 400 si el valorVenta es negativo")
    void createSale_WithNegativeValorVenta_ShouldReturnBadRequest() throws Exception {
        SaleRequestDTO invalidRequest = new SaleRequestDTO(
                1L, 2L, new BigDecimal("-100"), "Nota", PaymentMethod.TARJETA_CREDITO
        );

        mockMvc.perform(post("/api/venta")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(ventaCasosUso, never()).createSale(any());
    }

    // ── PUT /api/venta/actualizarventa/{id} ─────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("PUT /api/venta/actualizarventa/{id} - Debería actualizar una venta correctamente")
    void updateSale_WithValidDTO_ShouldReturnOk() throws Exception {
        Long id = 1L;
        SaleRequestDTO requestDTO = crearRequestDTOValido();
        Sale actualizada = crearSaleMock(id, 1L, 2L);
        SaleResponseDTO responseDTO = crearResponseDTOMock(id, 1L, 2L);

        when(ventaCasosUso.updateSale(eq(id), any(SaleRequestDTO.class))).thenReturn(actualizada);
        when(ventaMapper.toDTO(actualizada)).thenReturn(responseDTO);

        mockMvc.perform(put("/api/venta/actualizarventa/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idVenta").value(id))
                .andExpect(jsonPath("$.valorVenta").value(1500000))
                .andExpect(jsonPath("$.paymentMethod").value("TRANSFERENCIA"));
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    @DisplayName("PUT /api/venta/actualizarventa/{id} - Debería retornar 400 si el DTO no es válido")
    void updateSale_WithInvalidDTO_ShouldReturnBadRequest() throws Exception {
        Long id = 1L;
        SaleRequestDTO invalidRequest = new SaleRequestDTO(null, null, null, null, null);

        mockMvc.perform(put("/api/venta/actualizarventa/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(ventaCasosUso, never()).updateSale(any(), any());
    }
}