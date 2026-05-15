// qualification/infrastructure/events/EtapaCambiadaEvent.java
package com.terreneitors.backendclintec.qualification.infrastructure.events;

import com.terreneitors.backendclintec.opportunities.domain.StageOpportunity;

public record EtapaCambiadaEvent(Long clienteId, StageOpportunity etapaAnterior, StageOpportunity etapaNueva) {

}