package com.terreneitors.backendclintec.backup.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Backup {

    private Long id;
    private String nombreArchivo;
    private long tamanoBytes;
    private TypeBackup tipo;
    private LocalDateTime fechaCreacion;
    private String creadoPorEmail;
    private String creadoPorNombre;
}
