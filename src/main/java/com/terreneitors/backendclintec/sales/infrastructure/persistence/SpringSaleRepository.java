package com.terreneitors.backendclintec.sales.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringSaleRepository extends JpaRepository<SaleEntity,Long> {
        List<SaleEntity> findByIdAsesor(Long idAsesor);
        List<SaleEntity> findByIdOportunidad(Long idOportunidad);
}
