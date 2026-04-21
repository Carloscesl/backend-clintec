package com.terreneitors.backendclintec.opportunities.infrastructure.persistence;

import com.terreneitors.backendclintec.opportunities.application.port.out.OpportunityRepositoryPort;
import com.terreneitors.backendclintec.opportunities.domain.Opportunity;
import com.terreneitors.backendclintec.opportunities.infrastructure.persistence.mapper.OpportunityPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OpportunityRepositoryAdapter implements OpportunityRepositoryPort {
    private final SpringOpportunityRepository springOpportunityRepository;
    private final OpportunityPersistenceMapper mapper;

    public OpportunityRepositoryAdapter(SpringOpportunityRepository springOpportunityRepository, OpportunityPersistenceMapper mapper) {
        this.springOpportunityRepository = springOpportunityRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Opportunity> findAll() {
        return springOpportunityRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Opportunity> findById(Long id) {
        return springOpportunityRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Opportunity> findByAssessor(Long id) {
        return springOpportunityRepository.findByAsesorId(id).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Opportunity> findByIdClient(Long id) {
        return springOpportunityRepository.findByClienteId(id).stream().map(mapper::toDomain).toList();
    }

    @Override
    public Opportunity save(Opportunity opportunity) {
        OpportunityEntity entity = mapper.toEntity(opportunity);
        OpportunityEntity saved = springOpportunityRepository.save(entity);
        return mapper.toDomain(saved);
    }
}
