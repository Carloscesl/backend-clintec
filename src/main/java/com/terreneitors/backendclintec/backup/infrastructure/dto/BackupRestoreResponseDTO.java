package com.terreneitors.backendclintec.backup.infrastructure.dto;

import java.time.LocalDateTime;

public record BackupRestoreResponseDTO(
        String restauradoDesde,
        String respaldoDeSeguridad,
        LocalDateTime fecha
) {}
