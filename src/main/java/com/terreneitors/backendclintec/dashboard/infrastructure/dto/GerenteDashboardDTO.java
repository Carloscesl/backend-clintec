package com.terreneitors.backendclintec.dashboard.infrastructure.dto;

import java.math.BigDecimal;

public record GerenteDashboardDTO(
        long totalClientes,
        long totalVentas,
        long oportunidadesActivas,
        long oportunidadesGanadas,
        long oportunidadesPerdidas,
        BigDecimal valorOportunidadesGanadas,

        // Embudo — por Stage
        long enProspeccion,
        long enCalificacion,
        long enPropuesta,
        long enNegociacion,
        long enCierreGanado,
        long enCierrePerdido
) {
}
