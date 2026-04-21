package com.terreneitors.backendclintec.ventas.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringVentaRepository extends JpaRepository<VentaEntity,Long> {
        List<VentaEntity> findByIdAsesor(Long IdAsesor);
        List<VentaEntity> findByIdOportunidad(Long IdOportunidad);
}
