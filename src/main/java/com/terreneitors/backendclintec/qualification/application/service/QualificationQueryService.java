// qualification/application/service/QualificationQueryService.java
package com.terreneitors.backendclintec.qualification.application.service;

import com.terreneitors.backendclintec.qualification.application.port.in.QualificationQueryUseCase;
import com.terreneitors.backendclintec.qualification.application.port.out.QualificationHistoryRepositoryPort;
import com.terreneitors.backendclintec.qualification.application.port.out.QualificationRepositoryPort;
import com.terreneitors.backendclintec.qualification.domain.Qualification;
import com.terreneitors.backendclintec.qualification.domain.QualificationClient;
import com.terreneitors.backendclintec.qualification.domain.QualificationHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QualificationQueryService implements QualificationQueryUseCase {

    private final QualificationRepositoryPort        qualificationRepositoryPort;
    private final QualificationHistoryRepositoryPort historyRepositoryPort;

    @Override
    public List<QualificationClient> findByClasificacion(Qualification nivel) {
        log.info("[CALIFICACION_QUERY] clasificacion={}", nivel);
        return qualificationRepositoryPort.findByClasificacion(nivel);
    }

    @Override
    public List<QualificationClient> findTopN(int n) {
        log.info("[CALIFICACION_QUERY] top={}", n);
        return qualificationRepositoryPort.findTopN(n);
    }

    @Override
    public List<QualificationClient> findEnRiesgo() {
        // Clientes que bajaron puntaje en las últimas 48 horas
        LocalDateTime hace48h = LocalDateTime.now().minusHours(48);
        log.info("[CALIFICACION_QUERY] en-riesgo desde={}", hace48h);
        return qualificationRepositoryPort.findClientesQueBAjaronPuntaje(hace48h);
    }

    @Override
    public Map<Qualification, Long> distribucionPorNivel() {
        return qualificationRepositoryPort.findAll().stream()
                .collect(Collectors.groupingBy(
                        QualificationClient::getClasificacion,
                        Collectors.counting()
                ));
    }

    @Override
    public List<QualificationHistory> historialPorCliente(Long clienteId) {
        log.info("[CALIFICACION_HISTORIAL] clienteId={}", clienteId);
        return historyRepositoryPort.findByClienteId(clienteId);
    }
}