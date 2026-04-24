package com.terreneitors.backendclintec.interactions.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringInteractionRepository extends JpaRepository<InteractionEntity, Long> {
    List<InteractionEntity> findByClienteId(Long clienteId);
    List<InteractionEntity> findByOportunidadId(Long oportunidadId);
    List<InteractionEntity> findByUsuarioId(Long usuarioId);
}
