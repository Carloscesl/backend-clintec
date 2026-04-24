package com.terreneitors.backendclintec.qualification.infrastructure.persistence;

import com.terreneitors.backendclintec.qualification.application.port.out.QualificationRepositoryPort;
import com.terreneitors.backendclintec.qualification.domain.QualificationClient;
import com.terreneitors.backendclintec.qualification.infrastructure.persistence.Mapper.QualificationPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QualificationRepositoryADapter implements QualificationRepositoryPort {

    private final SpringQualificationRepository springQualificationRepository;
    private final QualificationPersistenceMapper mapper;

    @Override
    public QualificationClient save(QualificationClient qualificationClient) {
        return mapper.toDomain(springQualificationRepository.save(mapper.toEntity(qualificationClient)));
    }

    @Override
    public Optional<QualificationClient> findByClientId(Long clienteId) {
        return springQualificationRepository.findByClienteId(clienteId).map(mapper::toDomain);
    }

    @Override
    public List<QualificationClient> findAll() {
        return springQualificationRepository.findAll().stream().map(mapper::toDomain).toList();
    }
}
