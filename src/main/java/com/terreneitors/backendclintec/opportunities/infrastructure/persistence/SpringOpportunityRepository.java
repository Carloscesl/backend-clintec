package com.terreneitors.backendclintec.opportunities.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringOpportunityRepository extends JpaRepository<OpportunityEntity, Long> {
    List<OpportunityEntity> findByAsesorId(Long vendedorId);
    List<OpportunityEntity> findByClienteId(Long clienteId);
}
