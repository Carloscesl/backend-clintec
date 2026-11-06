package com.terreneitors.backendclintec.backup.application.service;

import com.terreneitors.backendclintec.backup.application.port.out.BackupRepositoryPort;
import com.terreneitors.backendclintec.backup.application.port.out.DatabaseBackupPort;
import com.terreneitors.backendclintec.backup.domain.Backup;
import com.terreneitors.backendclintec.backup.domain.RestoreResult;
import com.terreneitors.backendclintec.backup.domain.TypeBackup;
import com.terreneitors.backendclintec.shared.exception.InvalidStateException;
import com.terreneitors.backendclintec.shared.exception.ResourceNotFoundException;
import com.terreneitors.backendclintec.users.application.port.out.UserRepositoryPort;
import com.terreneitors.backendclintec.users.domain.Rol;
import com.terreneitors.backendclintec.users.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BackupServiceTest {

    @Mock
    private BackupRepositoryPort backupRepository;

    @Mock
    private DatabaseBackupPort databaseBackup;

    @Mock
    private UserRepositoryPort userRepository;

    @InjectMocks
    private BackupService backupService;

    private User admin;
    private Backup backupExistente;

    @BeforeEach
    void setUp() {
        admin = new User();
        admin.setId(1L);
        admin.setNombreUser("Carlos López");
        admin.setEmail("admin@clinteccrm.com");
        admin.setRol(Rol.ADMINISTRADOR);

        backupExistente = new Backup();
        backupExistente.setId(10L);
        backupExistente.setNombreArchivo("clintec_backup_20260101_100000.sql");
        backupExistente.setTipo(TypeBackup.MANUAL);
        backupExistente.setFechaCreacion(LocalDateTime.now().minusDays(1));
    }

    @Test
    void createBackup_guardaArchivoYRegistraQuienLoCreo() {
        when(databaseBackup.createDump(anyString())).thenReturn(2048L);
        when(userRepository.findByEmail("admin@clinteccrm.com")).thenReturn(Optional.of(admin));
        when(backupRepository.save(any(Backup.class))).thenAnswer(inv -> inv.getArgument(0));

        Backup resultado = backupService.createBackup("admin@clinteccrm.com");

        assertThat(resultado.getTipo()).isEqualTo(TypeBackup.MANUAL);
        assertThat(resultado.getTamanoBytes()).isEqualTo(2048L);
        assertThat(resultado.getCreadoPorEmail()).isEqualTo("admin@clinteccrm.com");
        assertThat(resultado.getCreadoPorNombre()).isEqualTo("Carlos López");
        assertThat(resultado.getNombreArchivo()).startsWith("clintec_backup_").endsWith(".sql");
        verify(databaseBackup).createDump(resultado.getNombreArchivo());
    }

    @Test
    void createBackup_siElUsuarioNoExisteUsaElEmailComoNombre() {
        when(databaseBackup.createDump(anyString())).thenReturn(10L);
        when(userRepository.findByEmail("fantasma@mail.com")).thenReturn(Optional.empty());
        when(backupRepository.save(any(Backup.class))).thenAnswer(inv -> inv.getArgument(0));

        Backup resultado = backupService.createBackup("fantasma@mail.com");

        assertThat(resultado.getCreadoPorNombre()).isEqualTo("fantasma@mail.com");
    }

    @Test
    void findAll_devuelveElHistorial() {
        when(backupRepository.findAll()).thenReturn(List.of(backupExistente));

        assertThat(backupService.findAll()).hasSize(1);
    }

    @Test
    void findById_noExiste_lanzaNotFound() {
        when(backupRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> backupService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void restore_creaBackupDeSeguridadAntesDeRestaurar() {
        when(backupRepository.findById(10L)).thenReturn(Optional.of(backupExistente));
        when(databaseBackup.exists(backupExistente.getNombreArchivo())).thenReturn(true);
        when(databaseBackup.createDump(anyString())).thenReturn(512L);
        when(userRepository.findByEmail("admin@clinteccrm.com")).thenReturn(Optional.of(admin));
        when(backupRepository.save(any(Backup.class))).thenAnswer(inv -> inv.getArgument(0));

        RestoreResult resultado = backupService.restore(10L, "admin@clinteccrm.com");

        assertThat(resultado.restauradoDesde()).isSameAs(backupExistente);
        assertThat(resultado.respaldoDeSeguridad().getTipo()).isEqualTo(TypeBackup.PRE_RESTAURACION);
        assertThat(resultado.respaldoDeSeguridad().getNombreArchivo()).startsWith("clintec_prerestore_");

        // El orden importa: primero el backup de seguridad, después la restauración
        InOrder orden = inOrder(databaseBackup);
        orden.verify(databaseBackup).createDump(anyString());
        orden.verify(databaseBackup).restore(backupExistente.getNombreArchivo());
    }

    @Test
    void restore_backupInexistente_lanzaNotFoundYNoTocaLaBD() {
        when(backupRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> backupService.restore(99L, "admin@clinteccrm.com"))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(databaseBackup, never()).restore(anyString());
        verify(databaseBackup, never()).createDump(anyString());
    }

    @Test
    void restore_archivoFaltante_lanzaInvalidStateYNoTocaLaBD() {
        when(backupRepository.findById(10L)).thenReturn(Optional.of(backupExistente));
        when(databaseBackup.exists(backupExistente.getNombreArchivo())).thenReturn(false);

        assertThatThrownBy(() -> backupService.restore(10L, "admin@clinteccrm.com"))
                .isInstanceOf(InvalidStateException.class);

        verify(databaseBackup, never()).restore(anyString());
        verify(databaseBackup, never()).createDump(anyString());
    }
}
