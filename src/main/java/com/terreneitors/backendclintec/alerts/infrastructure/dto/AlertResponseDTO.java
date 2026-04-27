package com.terreneitors.backendclintec.alerts.infrastructure.dto;

import com.terreneitors.backendclintec.alerts.domain.StateAlert;
import com.terreneitors.backendclintec.alerts.domain.TypeAlert;

import java.time.LocalDateTime;

public record AlertResponseDTO(
        Long                  id,
        Long                  clienteId,
        Long                  usuarioId,
        String                descripcion,
        TypeAlert tipo,
        StateAlert estado,
        LocalDateTime fechaVencimiento,
        LocalDateTime         fecha,
        LocalDateTime         fechaActualizacion
) {
}
