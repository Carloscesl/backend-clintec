package com.terreneitors.backendclintec.backup.application.port.out;

import org.springframework.core.io.Resource;

/** Motor que genera, guarda y restaura los archivos de backup de la base de datos. */
public interface DatabaseBackupPort {

    /** Vuelca la base de datos al archivo indicado y devuelve su tamaño en bytes. */
    long createDump(String fileName);

    /** Restaura la base de datos desde el archivo indicado. */
    void restore(String fileName);

    boolean exists(String fileName);

    Resource load(String fileName);
}
