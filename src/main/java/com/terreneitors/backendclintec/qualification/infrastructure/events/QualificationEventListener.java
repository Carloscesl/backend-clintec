package com.terreneitors.backendclintec.qualification.infrastructure.events;

import com.terreneitors.backendclintec.qualification.application.service.QualificationEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class QualificationEventListener {

    // ── Tabla de puntos ──────────────────────────────────────────────────
    private static final int PT_LLAMADA       = 3;
    private static final int PT_EMAIL         = 2;
    private static final int PT_REUNION       = 5;
    private static final int PT_WHATSAPP      = 1;
    private static final int PT_VISITA        = 7;
    private static final int PT_OTRO        = 5;
    private static final int PT_ETAPA_AVANCE  = 5;
    private static final int PT_VENTA_CERRADA = 20;

    private final QualificationEventService eventService;

    @EventListener
    public void onInteraccionCreada(InteraccionCreadaEvent event) {
        int puntos = switch (event.tipo()) {
            case LLAMADA  -> PT_LLAMADA;
            case EMAIL    -> PT_EMAIL;
            case REUNION  -> PT_REUNION;
            case WHATSAPP -> PT_WHATSAPP;
            case VISITA   -> PT_VISITA;
            case OTRO   -> PT_OTRO;
        };
        log.info("[EVENT] InteraccionCreada clienteId={} tipo={} +{}pts",
                event.clienteId(), event.tipo(), puntos);
        eventService.sumarPuntos(
                event.clienteId(),
                puntos,
                "INTERACCION_" + event.tipo().name()
        );
    }

    @EventListener
    public void onEtapaCambiada(EtapaCambiadaEvent event) {
        boolean avanza = event.etapaNueva().ordinal() > event.etapaAnterior().ordinal();
        if (!avanza) return;

        String motivo = "ETAPA_" + event.etapaAnterior() + "_A_" + event.etapaNueva();
        log.info("[EVENT] EtapaCambiada clienteId={} {} → {} +{}pts",
                event.clienteId(), event.etapaAnterior(), event.etapaNueva(), PT_ETAPA_AVANCE);
        eventService.sumarPuntos(event.clienteId(), PT_ETAPA_AVANCE, motivo);
    }

    @EventListener
    public void onVentaCerrada(VentaCerradaEvent event) {
        log.info("[EVENT] VentaCerrada clienteId={} +{}pts", event.clienteId(), PT_VENTA_CERRADA);
        eventService.sumarPuntos(event.clienteId(), PT_VENTA_CERRADA, "VENTA_CERRADA");
    }
}