package com.terreneitors.backendclintec.backup.domain;

/**
 * Resultado de una restauración: el backup que se restauró y el backup de
 * seguridad que se generó automáticamente justo antes de hacerlo.
 */
public record RestoreResult(Backup restauradoDesde, Backup respaldoDeSeguridad) {
}
