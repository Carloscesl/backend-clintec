package com.terreneitors.backendclintec.oportunidades.infrastructure.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OportunidadesResponseDTO(
        Long idOportunidades,
        Long clienteId,
        Long asesorrId,
        String descripcion,
        BigDecimal valorEstimado,
        String estado,
        LocalDateTime fechaCreacion
) {
}
