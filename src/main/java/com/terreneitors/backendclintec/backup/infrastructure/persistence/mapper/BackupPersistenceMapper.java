package com.terreneitors.backendclintec.backup.infrastructure.persistence.mapper;

import com.terreneitors.backendclintec.backup.domain.Backup;
import com.terreneitors.backendclintec.backup.domain.RestoreResult;
import com.terreneitors.backendclintec.backup.infrastructure.dto.BackupResponseDTO;
import com.terreneitors.backendclintec.backup.infrastructure.dto.BackupRestoreResponseDTO;
import com.terreneitors.backendclintec.backup.infrastructure.persistence.BackupEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BackupPersistenceMapper {

    public Backup toDomain(BackupEntity e) {
        Backup d = new Backup();
        d.setId(e.getId());
        d.setNombreArchivo(e.getNombreArchivo());
        d.setTamanoBytes(e.getTamanoBytes());
        d.setTipo(e.getTipo());
        d.setFechaCreacion(e.getFechaCreacion());
        d.setCreadoPorEmail(e.getCreadoPorEmail());
        d.setCreadoPorNombre(e.getCreadoPorNombre());
        return d;
    }

    public BackupEntity toEntity(Backup d) {
        BackupEntity e = new BackupEntity();
        e.setId(d.getId());
        e.setNombreArchivo(d.getNombreArchivo());
        e.setTamanoBytes(d.getTamanoBytes());
        e.setTipo(d.getTipo());
        e.setFechaCreacion(d.getFechaCreacion());
        e.setCreadoPorEmail(d.getCreadoPorEmail());
        e.setCreadoPorNombre(d.getCreadoPorNombre());
        return e;
    }

    public BackupResponseDTO toDTO(Backup b) {
        return new BackupResponseDTO(
                b.getId(),
                b.getNombreArchivo(),
                b.getTamanoBytes(),
                b.getTipo(),
                b.getFechaCreacion(),
                b.getCreadoPorNombre(),
                b.getCreadoPorEmail()
        );
    }

    public BackupRestoreResponseDTO toRestoreDTO(RestoreResult r) {
        return new BackupRestoreResponseDTO(
                r.restauradoDesde().getNombreArchivo(),
                r.respaldoDeSeguridad().getNombreArchivo(),
                LocalDateTime.now()
        );
    }
}
