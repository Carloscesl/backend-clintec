package com.terreneitors.backendclintec.backup.domain;

import com.terreneitors.backendclintec.shared.exception.BusinessException;

/**
 * Fallo técnico al generar o restaurar un backup (I/O, SQL...).
 * Se responde como 500 desde el GlobalExceptionHandler, pero con un mensaje útil.
 */
public class BackupException extends BusinessException {

    public BackupException(String mensaje) {
        super("BACKUP_FALLIDO", mensaje);
    }

    public BackupException(String mensaje, Throwable causa) {
        super("BACKUP_FALLIDO", mensaje);
        initCause(causa);
    }
}
