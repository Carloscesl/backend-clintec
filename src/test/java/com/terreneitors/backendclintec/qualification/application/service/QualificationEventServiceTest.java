package com.terreneitors.backendclintec.qualification.application.service;

import com.terreneitors.backendclintec.qualification.application.port.out.QualificationHistoryRepositoryPort;
import com.terreneitors.backendclintec.qualification.application.port.out.QualificationRepositoryPort;
import com.terreneitors.backendclintec.qualification.domain.QualificationClient;
import com.terreneitors.backendclintec.qualification.domain.QualificationHistory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QualificationEventServiceTest {

    @Mock
    private QualificationRepositoryPort qualificationRepositoryPort;

    @Mock
    private QualificationHistoryRepositoryPort historyRepositoryPort;

    @InjectMocks
    private QualificationEventService qualificationEventService;

    @Test
    @DisplayName("Debe actualizar puntaje y guardar historial cuando hay cambio")
    void sumarPuntos_ShouldUpdateAndSaveHistory_WhenPointsChange() {
        // Arrange
        Long clienteId = 1L;
        QualificationClient calificacion = new QualificationClient();
        calificacion.setClienteId(clienteId);
        calificacion.setPuntaje(50);

        when(qualificationRepositoryPort.findByClientId(clienteId))
                .thenReturn(Optional.of(calificacion));

        // Act
        qualificationEventService.sumarPuntos(clienteId, 10, "Compra realizada");

        // Assert
        verify(qualificationRepositoryPort).save(argThat(c -> c.getPuntaje() == 60));
        verify(historyRepositoryPort).save(any(QualificationHistory.class));
    }

    @Test
    @DisplayName("Debe aplicar límite (clamp) al puntaje máximo de 100")
    void sumarPuntos_ShouldClampToMax100() {
        Long clienteId = 1L;
        QualificationClient calificacion = new QualificationClient();
        calificacion.setPuntaje(95);

        when(qualificationRepositoryPort.findByClientId(clienteId))
                .thenReturn(Optional.of(calificacion));

        qualificationEventService.sumarPuntos(clienteId, 20, "Bonus extra");

        verify(qualificationRepositoryPort).save(argThat(c -> c.getPuntaje() == 100));
    }

    @Test
    @DisplayName("No debe guardar historial si el puntaje no cambia")
    void sumarPuntos_ShouldNotSaveHistory_WhenPointsDoNotChange() {
        Long clienteId = 1L;
        QualificationClient calificacion = new QualificationClient();
        calificacion.setPuntaje(100);

        when(qualificationRepositoryPort.findByClientId(clienteId))
                .thenReturn(Optional.of(calificacion));

        // Intento sumar puntos cuando ya está al máximo
        qualificationEventService.sumarPuntos(clienteId, 10, "Intento de sobrepasar límite");

        verify(historyRepositoryPort, never()).save(any());
    }
}