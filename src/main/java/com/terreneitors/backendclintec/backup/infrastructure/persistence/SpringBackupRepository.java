package com.terreneitors.backendclintec.backup.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringBackupRepository extends JpaRepository<BackupEntity, Long> {
    List<BackupEntity> findAllByOrderByFechaCreacionDesc();
}
