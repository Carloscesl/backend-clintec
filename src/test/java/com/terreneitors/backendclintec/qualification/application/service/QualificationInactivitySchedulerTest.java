package com.terreneitors.backendclintec.qualification.application.service;

import com.terreneitors.backendclintec.qualification.application.port.out.QualificationRepositoryPort;
import com.terreneitors.backendclintec.qualification.domain.QualificationClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QualificationInactivitySchedulerTest {

    @Mock
    private QualificationRepositoryPort qualificationRepositoryPort;

    @Mock
    private QualificationEventService eventService;

    @InjectMocks
    private QualificationInactivityScheduler scheduler;

    @Test
    @DisplayName("Debe penalizar a clientes que no han tenido actividad en más de 30 días")
    void revisarInactividad_ShouldPenalizeInactiveClients() {
        // Arrange: Crear un cliente inactivo
        QualificationClient inactivo = new QualificationClient();
        inactivo.setClienteId(1L);
        inactivo.setUltimaActualizacion(LocalDateTime.now().minusDays(31));

        when(qualificationRepositoryPort.findByUltimaActualizacionBefore(any(LocalDateTime.class)))
                .thenReturn(List.of(inactivo));

        // Act
        scheduler.revisarInactividad();

        // Assert: Verificar que el servicio de eventos fue llamado para penalizar
        verify(eventService).sumarPuntos(1L, -10, "INACTIVIDAD_AUTOMATICA");
    }

    @Test
    @DisplayName("No debe hacer nada si no hay clientes inactivos")
    void revisarInactividad_ShouldDoNothing_WhenNoInactiveClients() {
        when(qualificationRepositoryPort.findByUltimaActualizacionBefore(any(LocalDateTime.class)))
                .thenReturn(List.of());

        scheduler.revisarInactividad();

        verify(eventService, never()).sumarPuntos(anyLong(), anyInt(), anyString());
    }
}