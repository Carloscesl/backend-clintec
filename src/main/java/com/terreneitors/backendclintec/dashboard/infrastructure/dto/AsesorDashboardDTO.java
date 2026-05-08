package com.terreneitors.backendclintec.dashboard.infrastructure.dto;

import java.math.BigDecimal;

public record AsesorDashboardDTO(
        long misOportunidades,
        long misOportunidadesActivas,
        long misOportunidadesGanadas,
        long misOportunidadesPerdidas,
        BigDecimal miValorGanado,
        long misAlertasPendientes,
        // Su embudo personal
        long enProspeccion,
        long enCalificacion,
        long enPropuesta,
        long enNegociacion,
        long enCierreGanado,
        long enCierrePerdido
) {
}
