// qualification/infrastructure/persistence/QualificationHistoryRepositoryAdapter.java
package com.terreneitors.backendclintec.qualification.infrastructure.persistence;

import com.terreneitors.backendclintec.qualification.application.port.out.QualificationHistoryRepositoryPort;
import com.terreneitors.backendclintec.qualification.domain.QualificationHistory;
import com.terreneitors.backendclintec.qualification.infrastructure.persistence.mapper.QualificationHistoryPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QualificationHistoryRepositoryAdapter implements QualificationHistoryRepositoryPort {

    private final SpringQualificationHistoryRepository springHistoryRepository;
    private final QualificationHistoryPersistenceMapper mapper;

    @Override
    public QualificationHistory save(QualificationHistory history) {
        return mapper.toDomain(springHistoryRepository.save(mapper.toEntity(history)));
    }

    @Override
    public List<QualificationHistory> findByClienteId(Long clienteId) {
        return springHistoryRepository.findByClienteIdOrderByFechaDesc(clienteId)
                .stream().map(mapper::toDomain).toList();
    }
}