package com.terreneitors.backendclintec.backup.infrastructure.web;

import com.terreneitors.backendclintec.backup.application.port.in.BackupUseCase;
import com.terreneitors.backendclintec.backup.domain.Backup;
import com.terreneitors.backendclintec.backup.infrastructure.dto.BackupResponseDTO;
import com.terreneitors.backendclintec.backup.infrastructure.dto.BackupRestoreResponseDTO;
import com.terreneitors.backendclintec.backup.infrastructure.persistence.mapper.BackupPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/backups")
@RequiredArgsConstructor
public class BackupController {

    private final BackupUseCase backupUseCase;
    private final BackupPersistenceMapper mapper;

    // POST /api/backups — crea un backup nuevo
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<BackupResponseDTO> create(Authentication authentication) {
        Backup creado = backupUseCase.createBackup(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(creado));
    }

    // GET /api/backups — historial (más reciente primero)
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<BackupResponseDTO>> list() {
        return ResponseEntity.ok(
                backupUseCase.findAll().stream().map(mapper::toDTO).toList());
    }

    // POST /api/backups/{id}/restaurar — restaura la BD desde ese backup
    @PostMapping("/{id}/restaurar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<BackupRestoreResponseDTO> restore(@PathVariable Long id,
                                                            Authentication authentication) {
        return ResponseEntity.ok(
                mapper.toRestoreDTO(backupUseCase.restore(id, authentication.getName())));
    }

    // GET /api/backups/{id}/descargar — descarga el archivo .sql
    @GetMapping("/{id}/descargar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        Backup backup = backupUseCase.findById(id);
        Resource file = backupUseCase.loadFile(backup);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(backup.getNombreArchivo()).build().toString())
                .body(file);
    }
}
