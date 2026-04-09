package com.terreneitors.backendclintec.oportunidades.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringOportunidadesRepository extends JpaRepository<OportunidadesEntity, Long> {
    Optional<OportunidadesEntity> findByAsesorId(Long vendedorId);
    Optional<OportunidadesEntity> findByClienteId(Long clienteId);
}
