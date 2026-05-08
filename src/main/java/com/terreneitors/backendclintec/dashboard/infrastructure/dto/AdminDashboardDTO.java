package com.terreneitors.backendclintec.dashboard.infrastructure.dto;

import java.math.BigDecimal;

public record AdminDashboardDTO(
        long totalClientes,
        long totalUsuarios,
        long totalVentas,
        long alertasPendientes,
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
