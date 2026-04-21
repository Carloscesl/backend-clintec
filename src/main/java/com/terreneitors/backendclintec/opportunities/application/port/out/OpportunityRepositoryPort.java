package com.terreneitors.backendclintec.opportunities.application.port.out;

import com.terreneitors.backendclintec.opportunities.domain.Opportunity;

import java.util.List;
import java.util.Optional;

public interface OpportunityRepositoryPort {
    List<Opportunity> findAll();

    Optional<Opportunity> findById(Long id);
    List<Opportunity> findByAssessor(Long id);
    List<Opportunity> findByIdClient(Long id);

    Opportunity save (Opportunity opportunity);
}
