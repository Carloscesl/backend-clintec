package com.terreneitors.backendclintec.oportunidades.infrastructure.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OportunidadRequestDTO(

        @NotNull(message = "El cliente es obligatorio")
        Long clienteId,
        @NotNull(message = "El asesor es obligatorio")
        Long asesorId,

        String descripcion,
        @NotNull(message = "El valor estimado es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El valor debe ser mayor a 0")
        BigDecimal valorEstimado,

        @Future(message = "La fecha de cierre estimada debe ser futura")
        LocalDate fechaCierreEstimada

) {
}
