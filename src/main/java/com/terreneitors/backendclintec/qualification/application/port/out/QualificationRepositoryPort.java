package com.terreneitors.backendclintec.qualification.application.port.out;

import com.terreneitors.backendclintec.qualification.domain.QualificationClient;

import java.util.List;
import java.util.Optional;

public interface QualificationRepositoryPort {
    QualificationClient save(QualificationClient qualificationClient);
    Optional<QualificationClient> findByClientId(Long clienteId);
    List<QualificationClient> findAll();
}
