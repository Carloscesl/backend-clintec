package com.terreneitors.backendclintec.qualification.infrastructure.events;

import com.terreneitors.backendclintec.interactions.domain.TypeInteraction;
import com.terreneitors.backendclintec.opportunities.domain.StageOpportunity;
import com.terreneitors.backendclintec.qualification.application.service.QualificationEventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QualificationEventListenerTest {

    @Mock
    private QualificationEventService eventService;

    @InjectMocks
    private QualificationEventListener eventListener;

    @Test
    @DisplayName("Debe sumar puntos cuando se crea una interacción de tipo LLAMADA")
    void onInteraccionCreada_ShouldAddPointsForCall() {
        // Arrange
        InteraccionCreadaEvent event = new InteraccionCreadaEvent(1L, TypeInteraction.LLAMADA);

        // Act
        eventListener.onInteraccionCreada(event);

        // Assert: Verifica que sume 3 puntos (PT_LLAMADA)
        verify(eventService).sumarPuntos(1L, 3, "INTERACCION_LLAMADA");
    }

    @Test
    @DisplayName("Debe sumar puntos cuando una etapa avanza")
    void onEtapaCambiada_ShouldAddPoints_WhenStageAdvances() {
        // Arrange: Avanza de etapa 0 a etapa 1
        EtapaCambiadaEvent event = new EtapaCambiadaEvent(1L, StageOpportunity.PROSPECCION, StageOpportunity.NEGOCIACION);

        // Act
        eventListener.onEtapaCambiada(event);

        // Assert
        verify(eventService).sumarPuntos(1L, 5, "ETAPA_PROSPECCION_A_NEGOCIACION");
    }

    @Test
    @DisplayName("No debe sumar puntos si la etapa retrocede")
    void onEtapaCambiada_ShouldNotAddPoints_WhenStageRegresses() {
        EtapaCambiadaEvent event = new EtapaCambiadaEvent(1L, StageOpportunity.NEGOCIACION, StageOpportunity.PROSPECCION);

        eventListener.onEtapaCambiada(event);

        verify(eventService, never()).sumarPuntos(anyLong(), anyInt(), anyString());
    }

    @Test
    @DisplayName("Debe sumar puntos al cerrar una venta")
    void onVentaCerrada_ShouldAddPoints() {
        VentaCerradaEvent event = new VentaCerradaEvent(1L);

        eventListener.onVentaCerrada(event);

        verify(eventService).sumarPuntos(1L, 20, "VENTA_CERRADA");
    }
}