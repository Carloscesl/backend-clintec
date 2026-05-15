package com.terreneitors.backendclintec.qualification.application.port.out;

import com.terreneitors.backendclintec.qualification.domain.Qualification;
import com.terreneitors.backendclintec.qualification.domain.QualificationClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QualificationRepositoryPort {
    QualificationClient save(QualificationClient qualificationClient);
    Optional<QualificationClient> findByClientId(Long clienteId);
    List<QualificationClient> findAll();
    List<QualificationClient> findByClasificacion(Qualification clasificacion);
    List<QualificationClient> findTopN(int n);
    List<QualificationClient> findByUltimaActualizacionBefore(LocalDateTime threshold);
    List<QualificationClient> findClientesQueBAjaronPuntaje(LocalDateTime desde);
}
