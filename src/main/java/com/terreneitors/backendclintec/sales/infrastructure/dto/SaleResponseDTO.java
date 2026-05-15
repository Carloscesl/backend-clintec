package com.terreneitors.backendclintec.sales.infrastructure.dto;

import com.terreneitors.backendclintec.sales.domain.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaleResponseDTO(
        Long idVenta,
        Long idOportunidad,
        Long idAsesor,
        BigDecimal valorVenta,
        PaymentMethod paymentMethod,
        String notas,
        LocalDateTime fechaVenta,
        LocalDateTime fechaActualizacion
) {
}
