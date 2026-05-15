package com.terreneitors.backendclintec.qualification.application.port.in;

import com.terreneitors.backendclintec.qualification.domain.Qualification;
import com.terreneitors.backendclintec.qualification.domain.QualificationClient;
import com.terreneitors.backendclintec.qualification.domain.QualificationHistory;

import java.util.List;
import java.util.Map;

public interface QualificationQueryUseCase {
    List<QualificationClient> findByClasificacion(Qualification nivel);
    List<QualificationClient> findTopN(int n);
    List<QualificationClient> findEnRiesgo();
    Map<Qualification, Long> distribucionPorNivel();
    List<QualificationHistory> historialPorCliente(Long clienteId);
}
