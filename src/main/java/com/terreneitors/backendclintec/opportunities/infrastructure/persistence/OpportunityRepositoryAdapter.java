package com.terreneitors.backendclintec.opportunities.infrastructure.persistence;

import com.terreneitors.backendclintec.opportunities.application.port.out.OpportunityRepositoryPort;
import com.terreneitors.backendclintec.opportunities.domain.Opportunity;
import com.terreneitors.backendclintec.opportunities.domain.StageOpportunity;
import com.terreneitors.backendclintec.opportunities.domain.StatusOpportunity;
import com.terreneitors.backendclintec.opportunities.infrastructure.persistence.mapper.OpportunityPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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

    @Override
    public Long count() {
        return springOpportunityRepository.count();
    }

    @Override
    public Long countByEstado(StatusOpportunity estado) {
        return springOpportunityRepository.countByEstado(estado);
    }

    @Override
    public Long countByEtapaOportunidad(StageOpportunity etapa) {
        return springOpportunityRepository.countByEtapa(etapa);
    }

    @Override
    public Long countByAsesorId(Long asesorId) {
        return springOpportunityRepository.countByAsesorId(asesorId);
    }

    @Override
    public Long countByAsesorIdAndEstado(Long asesorId, StatusOpportunity estado) {
        return springOpportunityRepository.countByAsesorIdAndEstado(asesorId,estado);
    }

    @Override
    public Long countByAsesorIdAndEtapaOportunidad(Long asesorId, StageOpportunity etapa) {
        return springOpportunityRepository.countByAsesorIdAndEtapa(asesorId, etapa);
    }

    @Override
    public BigDecimal sumValorEstimadoByEstado(StatusOpportunity estado) {
        return springOpportunityRepository.sumValorEstimadoByEstado(estado);
    }
}
