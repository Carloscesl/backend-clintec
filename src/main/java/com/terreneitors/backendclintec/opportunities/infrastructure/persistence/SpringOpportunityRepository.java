package com.terreneitors.backendclintec.opportunities.infrastructure.persistence;

import com.terreneitors.backendclintec.opportunities.domain.StageOpportunity;
import com.terreneitors.backendclintec.opportunities.domain.StatusOpportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface SpringOpportunityRepository extends JpaRepository<OpportunityEntity, Long> {
    List<OpportunityEntity> findByAsesorId(Long vendedorId);
    List<OpportunityEntity> findByClienteId(Long clienteId);
    // opportunities/infrastructure/persistence/OpportunityJpaRepository.java
    long countByEstado(StatusOpportunity estado);
    long countByEtapa(StageOpportunity etapa);
    long countByAsesorId(Long asesorId);
    long countByAsesorIdAndEstado(Long asesorId, StatusOpportunity estado);
    long countByAsesorIdAndEtapa(Long asesorId, StageOpportunity etapa);

    @Query("SELECT COALESCE(SUM(o.valorEstimado), 0) FROM OpportunityEntity o WHERE o.estado = :estado")
    BigDecimal sumValorEstimadoByEstado(@Param("estado") StatusOpportunity estado);

    @Query("SELECT COALESCE(SUM(o.valorEstimado), 0) FROM OpportunityEntity o WHERE o.asesorId = :asesorId AND o.estado = :estado")
    BigDecimal sumValorEstimadoByAsesorIdAndEstado(@Param("asesorId") Long asesorId, @Param("estado") StatusOpportunity estado);
}
