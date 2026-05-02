package com.terreneitors.backendclintec.opportunities.application.port.out;

import com.terreneitors.backendclintec.opportunities.domain.Opportunity;
import com.terreneitors.backendclintec.opportunities.domain.StatusOpportunity;
import com.terreneitors.backendclintec.opportunities.domain.StageOpportunity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OpportunityRepositoryPort {
    List<Opportunity> findAll();

    Optional<Opportunity> findById(Long id);
    List<Opportunity> findByAssessor(Long id);
    List<Opportunity> findByIdClient(Long id);
    Long count();
    Opportunity save (Opportunity opportunity);
    Long countByEstado(StatusOpportunity estado);
    Long countByEtapaOportunidad(StageOpportunity etapa);
    Long countByAsesorId(Long asesorId);
    Long countByAsesorIdAndEstado(Long asesorId, StatusOpportunity estado);
    Long countByAsesorIdAndEtapaOportunidad(Long asesorId, StageOpportunity etapa);
    BigDecimal sumValorEstimadoByEstado(StatusOpportunity estado);
}
