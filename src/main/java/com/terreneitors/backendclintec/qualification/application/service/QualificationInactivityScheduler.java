// qualification/application/service/QualificationInactivityScheduler.java
package com.terreneitors.backendclintec.qualification.application.service;

import com.terreneitors.backendclintec.qualification.application.port.out.QualificationRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class QualificationInactivityScheduler {

    private static final int DIAS_UMBRAL  = 30;
    private static final int PENALIZACION = 10;

    private final QualificationRepositoryPort qualificationRepositoryPort;
    private final QualificationEventService   eventService;

    // Todos los días a las 8:00 AM
    @Scheduled(cron = "0 0 8 * * *")
    public void revisarInactividad() {
        LocalDateTime umbral = LocalDateTime.now().minusDays(DIAS_UMBRAL);
        log.info("[SCHEDULER_INACTIVIDAD] Revisando clientes sin actividad desde {}", umbral);

        qualificationRepositoryPort.findByUltimaActualizacionBefore(umbral)
                .forEach(cliente -> {
                    log.warn("[SCHEDULER_INACTIVIDAD] clienteId={} inactivo → -{} pts",
                            cliente.getClienteId(), PENALIZACION);
                    eventService.sumarPuntos(
                            cliente.getClienteId(),
                            -PENALIZACION,
                            "INACTIVIDAD_AUTOMATICA"
                    );
                });
    }
}