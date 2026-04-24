package com.terreneitors.backendclintec.qualification.infrastructure.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record QualificationRequestDTO(
        @NotNull(message = "El puntaje es obligatorio")
        @Min(value = 0,   message = "El puntaje mínimo es 0")
        @Max(value = 100, message = "El puntaje máximo es 100")
        Integer puntaje
) {
}
