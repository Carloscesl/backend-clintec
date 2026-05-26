// qualification/application/service/QualificationEventService.java
package com.terreneitors.backendclintec.qualification.application.service;

import com.terreneitors.backendclintec.qualification.application.port.out.QualificationHistoryRepositoryPort;
import com.terreneitors.backendclintec.qualification.application.port.out.QualificationRepositoryPort;
import com.terreneitors.backendclintec.qualification.domain.QualificationClient;
import com.terreneitors.backendclintec.qualification.domain.QualificationHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class QualificationEventService {

    private final QualificationRepositoryPort    qualificationRepositoryPort;
    private final QualificationHistoryRepositoryPort historyRepositoryPort;

    @Transactional
    public void sumarPuntos(Long clienteId, int delta, String motivo) {
        log.info("[CALIFICACION_EVENTO] clienteId={} | delta={} | motivo={}", clienteId, delta, motivo);

        QualificationClient calificacion = qualificationRepositoryPort
                .findByClientId(clienteId)
                .orElseGet(() -> {
                    log.info("[CALIFICACION_AUTO_CREAR] clienteId={}", clienteId);
                    QualificationClient nueva = new QualificationClient();
                    nueva.setClienteId(clienteId);
                    nueva.setUltimaActualizacion(LocalDateTime.now());
                    return nueva;
                });

        int anterior = calificacion.getPuntaje();
        int nuevo = (int) Math.clamp((long) anterior + delta, 0L, 100L);

        calificacion.setPuntaje(nuevo);
        calificacion.setUltimaActualizacion(LocalDateTime.now());
        qualificationRepositoryPort.save(calificacion);

        if (anterior != nuevo) {
            historyRepositoryPort.save(
                    new QualificationHistory(clienteId, anterior, nuevo, motivo));
            log.info("[CALIFICACION_ACTUALIZADA] clienteId={} | {}→{} | motivo={}",
                    clienteId, anterior, nuevo, motivo);
        }
    }
}