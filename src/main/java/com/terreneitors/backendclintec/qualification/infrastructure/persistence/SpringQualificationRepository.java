package com.terreneitors.backendclintec.qualification.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringQualificationRepository extends JpaRepository<QualificationEntity, Long> {
    Optional<QualificationEntity> findByClienteId(Long clienteId);
}
