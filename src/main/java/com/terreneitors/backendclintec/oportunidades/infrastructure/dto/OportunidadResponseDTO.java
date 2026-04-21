package com.terreneitors.backendclintec.oportunidades.infrastructure.dto;

import com.terreneitors.backendclintec.oportunidades.domain.EstadoOportunidad;
import com.terreneitors.backendclintec.oportunidades.domain.EtapaOportunidad;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record OportunidadResponseDTO(
        Long idOportunidades,
        Long clienteId,
        Long asesorrId,
        String descripcion,
        BigDecimal valorEstimado,
        Integer probalidad,
        EtapaOportunidad etapa,
        EstadoOportunidad estado,
        boolean esPotencial,
        LocalDate fechaCierreEstimada,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion
) {
}
