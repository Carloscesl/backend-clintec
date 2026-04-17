package com.terreneitors.backendclintec.ventas.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringVentaRepository extends JpaRepository<ventaEntity,Long> {
        List<ventaEntity> findByIdAsesor(Long IdAsesor);
        List<ventaEntity> findByIdOportuniad(Long IdOportunidad);
}
