package com.terreneitors.backendclintec.backup.domain;

public enum TypeBackup {
    /** Creado por el administrador desde la pantalla de backups. */
    MANUAL,
    /** Creado automáticamente justo antes de restaurar otro backup (red de seguridad). */
    PRE_RESTAURACION
}
