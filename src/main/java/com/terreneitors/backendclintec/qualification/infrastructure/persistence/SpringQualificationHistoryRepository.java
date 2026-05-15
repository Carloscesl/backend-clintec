// qualification/infrastructure/persistence/SpringQualificationHistoryRepository.java
package com.terreneitors.backendclintec.qualification.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringQualificationHistoryRepository
        extends JpaRepository<QualificationHistoryEntity, Long> {

    List<QualificationHistoryEntity> findByClienteIdOrderByFechaDesc(Long clienteId);
}