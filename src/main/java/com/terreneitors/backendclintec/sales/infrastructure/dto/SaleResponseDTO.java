package com.terreneitors.backendclintec.sales.infrastructure.dto;

import com.terreneitors.backendclintec.sales.domain.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaleResponseDTO(
        Long idVentas,
        Long oportunidadId,
        Long vendedorId,
        BigDecimal valor,
        PaymentMethod paymentMethod,
        String notas,
        LocalDateTime fechaVenta,
        LocalDateTime fechaActualizacion
) {
}
