package com.terreneitors.backendclintec.interactions.application.port.out;

import com.terreneitors.backendclintec.interactions.domain.Interaction;

import java.util.List;
import java.util.Optional;

public interface InteractionRepositoryPort {
    Interaction save(Interaction interaccion);
    Optional<Interaction> findById(Long id);
    List<Interaction> findAll();
    List<Interaction> findByClienteId(Long clienteId);
    List<Interaction> findByOportunidadId(Long oportunidadId);
    List<Interaction> findByUsuarioId(Long usuarioId);
}
