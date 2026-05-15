// qualification/infrastructure/dto/QualificationHistoryResponseDTO.java
package com.terreneitors.backendclintec.qualification.infrastructure.dto;

import com.terreneitors.backendclintec.qualification.domain.Qualification;
import java.time.LocalDateTime;

public record QualificationHistoryResponseDTO(
        Long id,
        Long clienteId,
        int puntajeAnterior,
        int puntajeNuevo,
        String motivo,
        LocalDateTime fecha
) {}