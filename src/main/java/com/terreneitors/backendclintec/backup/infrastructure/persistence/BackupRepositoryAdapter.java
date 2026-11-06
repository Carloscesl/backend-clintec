package com.terreneitors.backendclintec.backup.infrastructure.persistence;

import com.terreneitors.backendclintec.backup.application.port.out.BackupRepositoryPort;
import com.terreneitors.backendclintec.backup.domain.Backup;
import com.terreneitors.backendclintec.backup.infrastructure.persistence.mapper.BackupPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BackupRepositoryAdapter implements BackupRepositoryPort {

    private final SpringBackupRepository springRepository;
    private final BackupPersistenceMapper mapper;

    @Override
    public Backup save(Backup backup) {
        return mapper.toDomain(springRepository.save(mapper.toEntity(backup)));
    }

    @Override
    public List<Backup> findAll() {
        return springRepository.findAllByOrderByFechaCreacionDesc()
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Backup> findById(Long id) {
        return springRepository.findById(id).map(mapper::toDomain);
    }
}
