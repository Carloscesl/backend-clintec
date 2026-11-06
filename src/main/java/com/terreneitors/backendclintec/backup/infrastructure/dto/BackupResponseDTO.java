package com.terreneitors.backendclintec.backup.infrastructure.dto;

import com.terreneitors.backendclintec.backup.domain.TypeBackup;

import java.time.LocalDateTime;

public record BackupResponseDTO(
        Long id,
        String nombreArchivo,
        long tamanoBytes,
        TypeBackup tipo,
        LocalDateTime fechaCreacion,
        String creadoPorNombre,
        String creadoPorEmail
) {}
