package com.terreneitors.backendclintec.ventas.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringVentaRepository extends JpaRepository<ventaEntity,Long> {
        Optional<ventaEntity> findByIdAsesor(Long IdAsesor);
        Optional<ventaEntity> findByIdOportuniad(Long IdOportunidad);
}
