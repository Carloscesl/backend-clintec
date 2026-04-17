package com.terreneitors.backendclintec.ventas.infrastructure.dto;

import com.terreneitors.backendclintec.ventas.domain.MetodoPago;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VentaResponseDTO(
        Long idVentas,
        Long oportunidadId,
        Long vendedorId,
        BigDecimal valor,
        MetodoPago metodoPago,
        String notas,
        LocalDateTime fechaVenta,
        LocalDateTime fechaActualizacion
) {
}
