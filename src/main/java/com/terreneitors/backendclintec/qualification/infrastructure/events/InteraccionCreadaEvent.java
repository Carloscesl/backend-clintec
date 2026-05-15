// qualification/infrastructure/events/InteraccionCreadaEvent.java
package com.terreneitors.backendclintec.qualification.infrastructure.events;

import com.terreneitors.backendclintec.interactions.domain.TypeInteraction;

public record InteraccionCreadaEvent(Long clienteId, TypeInteraction tipo) {

}