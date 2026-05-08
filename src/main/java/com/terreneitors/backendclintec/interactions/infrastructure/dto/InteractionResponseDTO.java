package com.terreneitors.backendclintec.interactions.infrastructure.dto;

import com.terreneitors.backendclintec.interactions.domain.TypeInteraction;

import java.time.LocalDateTime;

public record InteractionResponseDTO(
        Long id,
        Long clienteId,
        Long usuarioId,
        Long oportunidadId,
        TypeInteraction tipo,
        String nota,
        LocalDateTime echa,
        LocalDateTime fechaActualizacion
) {
}
