package com.terreneitors.backendclintec.ventas.infrastructure.dto;

import com.terreneitors.backendclintec.ventas.domain.MetodoPago;

import java.math.BigDecimal;

public record VentaRequestDTO(
        Long idOportunidad,
        Long idAsesor,
        BigDecimal valorVenta,
        String notas,
        MetodoPago metodoPago
) {
}
