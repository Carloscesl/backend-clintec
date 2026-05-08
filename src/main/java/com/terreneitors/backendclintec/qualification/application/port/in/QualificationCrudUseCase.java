package com.terreneitors.backendclintec.qualification.application.port.in;

import com.terreneitors.backendclintec.qualification.domain.QualificationClient;

import java.util.List;
import java.util.Optional;

public interface QualificationCrudUseCase {
    QualificationClient createQualificationInitial(Long clienteId);
    Optional<QualificationClient> findByClientId(Long clienteId);
    List<QualificationClient> findAll();
    QualificationClient updateScore(Long clienteId, int newEscore);
}
