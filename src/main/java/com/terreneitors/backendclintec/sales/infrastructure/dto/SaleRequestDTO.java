package com.terreneitors.backendclintec.sales.infrastructure.dto;

import com.terreneitors.backendclintec.sales.domain.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record SaleRequestDTO(
        @NotNull(message = "La oportunidad es obligatoria")
        Long idOportunidad,

        @NotNull(message = "El asesor es obligatorio")
        Long idAsesor,

        @NotNull(message = "El valor de la venta es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false,
                message = "El valor de la venta debe ser mayor a 0")
        BigDecimal valorVenta,

        String notas, // ← opcional, no necesita validación

        @NotNull(message = "El método de pago es obligatorio")
        PaymentMethod paymentMethod
) {
}
