package com.terreneitors.backendclintec.backup.application.port.out;

import com.terreneitors.backendclintec.backup.domain.Backup;

import java.util.List;
import java.util.Optional;

/** Persistencia del historial (metadatos) de backups. */
public interface BackupRepositoryPort {
    Backup save(Backup backup);
    List<Backup> findAll();
    Optional<Backup> findById(Long id);
}
