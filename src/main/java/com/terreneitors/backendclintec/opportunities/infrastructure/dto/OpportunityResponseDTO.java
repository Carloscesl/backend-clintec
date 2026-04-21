package com.terreneitors.backendclintec.opportunities.infrastructure.dto;

import com.terreneitors.backendclintec.opportunities.domain.StatusOpportunity;
import com.terreneitors.backendclintec.opportunities.domain.StageOpportunity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record OpportunityResponseDTO(
        Long idOportunidades,
        Long clienteId,
        Long asesorrId,
        String descripcion,
        BigDecimal valorEstimado,
        Integer probalidad,
        StageOpportunity etapa,
        StatusOpportunity estado,
        boolean esPotencial,
        LocalDate fechaCierreEstimada,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion
) {
}
