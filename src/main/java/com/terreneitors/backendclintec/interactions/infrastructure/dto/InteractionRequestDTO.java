package com.terreneitors.backendclintec.interactions.infrastructure.dto;

import com.terreneitors.backendclintec.interactions.domain.TypeInteraction;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record InteractionRequestDTO(
        @NotNull(message = "El cliente es obligatorio")
        Long clienteId,

        @NotNull(message = "El usuario es obligatorio")
        Long usuarioId,

        @NotNull(message = "La oportunidad es obligatoria")
        Long oportunidadId,

        @NotNull(message = "El tipo de interacción es obligatorio")
        TypeInteraction tipo,

        @Size(max = 1000, message = "La nota no puede superar 1000 caracteres")
        String nota
) {
}
