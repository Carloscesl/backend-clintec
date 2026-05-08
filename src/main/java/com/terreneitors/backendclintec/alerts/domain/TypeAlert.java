package com.terreneitors.backendclintec.alerts.domain;

public enum TypeAlert {
    INACTIVIDAD,   // cliente sin interacciones en 15 días
    VENCIMIENTO,   // oportunidad con fecha de cierre vencida
    SEGUIMIENTO,   // oportunidad en NEGOCIACIÓN sin cambios en 15 días
    OPORTUNIDAD
}
