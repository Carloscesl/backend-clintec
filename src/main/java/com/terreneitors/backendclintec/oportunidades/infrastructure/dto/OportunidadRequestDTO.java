package com.terreneitors.backendclintec.oportunidades.infrastructure.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OportunidadRequestDTO(

        @NotNull(message = "El cliente es obligatorio")
        Long clienteId,
        @NotNull(message = "El asesor es obligatorio")
        Long asesorId,

        @Size(max = 500, message = "La descripción no puede superar 500 caracteres")
        String descripcion,

        @NotNull(message = "El valor estimado es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false,
                message = "El valor estimado debe ser mayor a 0")
        BigDecimal valorEstimado,

        @NotNull(message = "La fecha de cierre estimada es obligatoria")
        @Future(message = "La fecha de cierre estimada debe ser una fecha futura")
        LocalDate fechaCierreEstimada

) {
}
