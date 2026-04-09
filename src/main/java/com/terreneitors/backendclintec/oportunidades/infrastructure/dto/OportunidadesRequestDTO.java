package com.terreneitors.backendclintec.oportunidades.infrastructure.dto;

import com.terreneitors.backendclintec.oportunidades.domain.EstadoOportunidad;

import java.math.BigDecimal;

public record OportunidadesRequestDTO(
        Long clienteId,
        Long asesorId,
        String descripcion,
        BigDecimal valorEstimado,
        EstadoOportunidad estado
) {
}
