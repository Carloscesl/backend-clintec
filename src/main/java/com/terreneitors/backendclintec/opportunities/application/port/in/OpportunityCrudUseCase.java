package com.terreneitors.backendclintec.opportunities.application.port.in;

import com.terreneitors.backendclintec.opportunities.domain.StageOpportunity;
import com.terreneitors.backendclintec.opportunities.domain.Opportunity;
import com.terreneitors.backendclintec.opportunities.infrastructure.dto.OpportunityRequestDTO;

import java.util.List;
import java.util.Optional;

public interface OpportunityCrudUseCase {
    List<Opportunity> findAll();

    Optional<Opportunity> findById(Long id);
    List<Opportunity> findByIdAssessor(Long id);
    List<Opportunity> findByIdClient(Long id);

    Opportunity createOpportunities(OpportunityRequestDTO oportunidad);
    Opportunity updateOpportunities(Long id, OpportunityRequestDTO oportunidad);
    Opportunity changeStage(Long id, StageOpportunity nuevaEtapa);
    Opportunity adjustProbability(Long id, int probabilidad);
    void closeAsWon(Long id);
    void closeAsLost(Long id);
}
