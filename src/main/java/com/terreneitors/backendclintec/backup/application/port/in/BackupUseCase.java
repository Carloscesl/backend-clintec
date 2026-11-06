package com.terreneitors.backendclintec.backup.application.port.in;

import com.terreneitors.backendclintec.backup.domain.Backup;
import com.terreneitors.backendclintec.backup.domain.RestoreResult;
import org.springframework.core.io.Resource;

import java.util.List;

public interface BackupUseCase {

    /** Genera un backup completo de la base de datos a nombre de quien lo solicita. */
    Backup createBackup(String creatorEmail);

    /** Historial de backups, del más reciente al más antiguo. */
    List<Backup> findAll();

    Backup findById(Long id);

    /** Restaura la base de datos desde un backup del historial. */
    RestoreResult restore(Long backupId, String requesterEmail);

    /** Archivo físico del backup, para descargarlo. */
    Resource loadFile(Backup backup);
}
