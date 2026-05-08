package com.terreneitors.backendclintec.qualification.infrastructure.dto;

import com.terreneitors.backendclintec.qualification.domain.Qualification;

import java.time.LocalDateTime;

public record QualificationResponseDTO(
        Long id,
        Long clienteId,
        int puntaje,
        Qualification clasificacion,
        LocalDateTime ultimaActualizacion
) {
}
