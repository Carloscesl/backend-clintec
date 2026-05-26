package com.terreneitors.backendclintec.opportunities.infrastructure.persistence.mapper;

import com.terreneitors.backendclintec.opportunities.domain.Opportunity;
import com.terreneitors.backendclintec.opportunities.infrastructure.dto.OpportunityResponseDTO;
import com.terreneitors.backendclintec.opportunities.infrastructure.persistence.OpportunityEntity;
import org.springframework.stereotype.Component;

@Component
public class OpportunityPersistenceMapper {
    public Opportunity toDomain(OpportunityEntity entity){
        Opportunity o = new Opportunity();
        o.setIdOportunidad(entity.getId());
        o.setClienteId(entity.getClienteId());
        o.setAsesorId(entity.getAsesorId());
        o.setDescripcion(entity.getDescripcion());
        o.setValorEstimado(entity.getValorEstimado());
        o.setStageOpportunity(entity.getEtapa());                   // dispara probabilidad default
        o.setProbabilidad(entity.getProbabilidad());     // sobreescribe con el valor real
        o.setEstado(entity.getEstado());
        o.setFechaEstimadaCierre(entity.getFechaCierreEstimada());
        o.setFechaCreacion(entity.getFechaCreacion());
        o.setFechaActualizacion(entity.getFechaActualizacion());
        o.setVentaId(entity.getVentaId());
        return o;
    }
    public OpportunityEntity toEntity(Opportunity o){
        OpportunityEntity entity = new OpportunityEntity();
        entity.setId(o.getIdOportunidad());
        entity.setClienteId(o.getClienteId());
        entity.setAsesorId(o.getAsesorId());
        entity.setDescripcion(o.getDescripcion());
        entity.setValorEstimado(o.getValorEstimado());
        entity.setProbabilidad(o.getProbabilidad());
        entity.setEtapa(o.getStageOpportunity());
        entity.setEstado(o.getEstado());
        entity.setFechaCierreEstimada(o.getFechaEstimadaCierre());
        entity.setFechaCreacion(o.getFechaCreacion());
        entity.setFechaActualizacion(o.getFechaActualizacion());
        entity.setVentaId(o.getVentaId());
        return entity;
    }
    public OpportunityResponseDTO toDTO(Opportunity o) {
        return new OpportunityResponseDTO(
                o.getIdOportunidad(),
                o.getClienteId(),
                o.getAsesorId(),
                o.getDescripcion(),
                o.getValorEstimado(),
                o.getProbabilidad(),
                o.getStageOpportunity(),
                o.getEstado(),
                o.esPotencial(),
                o.getVentaId(),
                o.getFechaEstimadaCierre(),
                o.getFechaCreacion(),
                o.getFechaActualizacion()
        );
    }

}
