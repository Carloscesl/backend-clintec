package com.terreneitors.backendclintec.backup.infrastructure.persistence;

import com.terreneitors.backendclintec.backup.domain.TypeBackup;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * OJO: esta tabla NO se incluye en los backups ni se toca al restaurar
 * (ver MysqlDatabaseBackupAdapter.EXCLUDED_TABLES). Si el nombre cambia,
 * hay que actualizar esa lista.
 */
@Getter
@Setter
@Entity
@Table(name = "backup_historial")
public class BackupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_archivo", nullable = false, unique = true, length = 150)
    private String nombreArchivo;

    @Column(name = "tamano_bytes", nullable = false)
    private long tamanoBytes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TypeBackup tipo;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "creado_por_email", nullable = false, length = 120)
    private String creadoPorEmail;

    @Column(name = "creado_por_nombre", nullable = false, length = 100)
    private String creadoPorNombre;
}
