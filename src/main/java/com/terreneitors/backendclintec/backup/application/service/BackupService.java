package com.terreneitors.backendclintec.backup.application.service;

import com.terreneitors.backendclintec.backup.application.port.in.BackupUseCase;
import com.terreneitors.backendclintec.backup.application.port.out.BackupRepositoryPort;
import com.terreneitors.backendclintec.backup.application.port.out.DatabaseBackupPort;
import com.terreneitors.backendclintec.backup.domain.Backup;
import com.terreneitors.backendclintec.backup.domain.RestoreResult;
import com.terreneitors.backendclintec.backup.domain.TypeBackup;
import com.terreneitors.backendclintec.shared.exception.InvalidStateException;
import com.terreneitors.backendclintec.shared.exception.ResourceNotFoundException;
import com.terreneitors.backendclintec.users.application.port.out.UserRepositoryPort;
import com.terreneitors.backendclintec.users.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
@RequiredArgsConstructor
public class BackupService implements BackupUseCase {

    private static final DateTimeFormatter FILE_TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private final BackupRepositoryPort backupRepository;
    private final DatabaseBackupPort databaseBackup;
    private final UserRepositoryPort userRepository;

    // Solo se permite una operación pesada (backup o restauración) a la vez
    private final ReentrantLock operationLock = new ReentrantLock();

    @Override
    public Backup createBackup(String creatorEmail) {
        log.info("[BACKUP_CREAR] usuario={}", creatorEmail);
        acquireLock();
        try {
            Backup backup = doCreate(creatorEmail, TypeBackup.MANUAL);
            log.info("[BACKUP_CREADO] id={} | archivo={} | bytes={}",
                    backup.getId(), backup.getNombreArchivo(), backup.getTamanoBytes());
            return backup;
        } finally {
            operationLock.unlock();
        }
    }

    @Override
    public List<Backup> findAll() {
        return backupRepository.findAll();
    }

    @Override
    public Backup findById(Long id) {
        return backupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Backup", "id", id));
    }

    @Override
    public RestoreResult restore(Long backupId, String requesterEmail) {
        log.warn("[BACKUP_RESTAURAR] id={} | usuario={}", backupId, requesterEmail);

        Backup origen = findById(backupId);
        if (!databaseBackup.exists(origen.getNombreArchivo())) {
            throw new InvalidStateException(
                    "El archivo del backup '" + origen.getNombreArchivo() + "' ya no existe en el servidor");
        }

        acquireLock();
        try {
            // Red de seguridad: se guarda el estado actual antes de sobrescribirlo
            Backup seguridad = doCreate(requesterEmail, TypeBackup.PRE_RESTAURACION);
            log.info("[BACKUP_SEGURIDAD_CREADO] archivo={}", seguridad.getNombreArchivo());

            databaseBackup.restore(origen.getNombreArchivo());

            log.warn("[BACKUP_RESTAURADO] origen={} | seguridad={} | usuario={}",
                    origen.getNombreArchivo(), seguridad.getNombreArchivo(), requesterEmail);
            return new RestoreResult(origen, seguridad);
        } finally {
            operationLock.unlock();
        }
    }

    @Override
    public Resource loadFile(Backup backup) {
        return databaseBackup.load(backup.getNombreArchivo());
    }

    // ── Privados ─────────────────────────────────────────────────

    private Backup doCreate(String creatorEmail, TypeBackup tipo) {
        LocalDateTime ahora = LocalDateTime.now();
        String fileName = buildFileName(tipo, ahora);

        long size = databaseBackup.createDump(fileName);

        Backup backup = new Backup();
        backup.setNombreArchivo(fileName);
        backup.setTamanoBytes(size);
        backup.setTipo(tipo);
        backup.setFechaCreacion(ahora);
        backup.setCreadoPorEmail(creatorEmail);
        // Se guarda el nombre como "foto": el historial no depende de que el usuario siga existiendo
        backup.setCreadoPorNombre(
                userRepository.findByEmail(creatorEmail).map(User::getNombreUser).orElse(creatorEmail));

        return backupRepository.save(backup);
    }

    private String buildFileName(TypeBackup tipo, LocalDateTime fecha) {
        String prefijo = tipo == TypeBackup.PRE_RESTAURACION ? "clintec_prerestore_" : "clintec_backup_";
        return prefijo + FILE_TIMESTAMP.format(fecha) + ".sql";
    }

    private void acquireLock() {
        if (!operationLock.tryLock()) {
            throw new InvalidStateException("Ya hay un backup o una restauración en curso. Intenta de nuevo en unos segundos");
        }
    }
}
