package com.terreneitors.backendclintec.qualification.application.port.out;

import com.terreneitors.backendclintec.qualification.domain.QualificationHistory;

import java.util.List;

public interface QualificationHistoryRepositoryPort {
    QualificationHistory save(QualificationHistory history);
    List<QualificationHistory> findByClienteId(Long clienteId);
}
