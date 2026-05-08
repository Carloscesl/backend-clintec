package com.terreneitors.backendclintec.interactions.application.port.in;

import com.terreneitors.backendclintec.interactions.domain.Interaction;
import com.terreneitors.backendclintec.interactions.infrastructure.dto.InteractionRequestDTO;

import java.util.List;
import java.util.Optional;

public interface InteractionCrudUseCase {
    List<Interaction> findAll();
    Optional<Interaction> findById(Long id);
    List<Interaction> findByIdClient(Long clienteId);
    List<Interaction> findByIdOpportunities(Long oportunidadId);
    List<Interaction> findByIdUser(Long usuarioId);

    Interaction createInteraction (InteractionRequestDTO dto);
    Interaction updateInteraction(Long id, InteractionRequestDTO dto);
}
