package com.terreneitors.backendclintec.oportunidades.infrastructure.persistence.mapper;

import com.terreneitors.backendclintec.oportunidades.domain.Oportunidad;
import com.terreneitors.backendclintec.oportunidades.infrastructure.dto.OportunidadResponseDTO;
import com.terreneitors.backendclintec.oportunidades.infrastructure.persistence.OportunidadEntity;
import org.springframework.stereotype.Component;

@Component
public class OportunidadesPersistenceMapper {
    public Oportunidad toDomain(OportunidadEntity entity){
        Oportunidad o = new Oportunidad();
        o.setIdOportunidades(entity.getId());
        o.setClienteId(entity.getClienteId());
        o.setAsesorId(entity.getAsesorId());
        o.setDescripcion(entity.getDescripcion());
        o.setValorEstimado(entity.getValorEstimado());
        o.setEtapaOportunidad(entity.getEtapa());                   // dispara probabilidad default
        o.setProbabilidad(entity.getProbabilidad());     // sobreescribe con el valor real
        o.setEstado(entity.getEstado());
        o.setFechaEstimadaCierre(entity.getFechaCierreEstimada());
        o.setFechaCreacion(entity.getFechaCreacion());
        o.setFechaActualizacion(entity.getFechaActualizacion());
        return o;
    }
    public OportunidadEntity toEntity(Oportunidad o){
        OportunidadEntity entity = new OportunidadEntity();
        entity.setId(o.getIdOportunidades());
        entity.setClienteId(o.getClienteId());
        entity.setAsesorId(o.getAsesorId());
        entity.setDescripcion(o.getDescripcion());
        entity.setValorEstimado(o.getValorEstimado());
        entity.setProbabilidad(o.getProbabilidad());
        entity.setEtapa(o.getEtapaOportunidad());
        entity.setEstado(o.getEstado());
        entity.setFechaCierreEstimada(o.getFechaEstimadaCierre());
        entity.setFechaCreacion(o.getFechaCreacion());
        entity.setFechaActualizacion(o.getFechaActualizacion());
        return entity;
    }
    public OportunidadResponseDTO toDTO(Oportunidad o) {
        return new OportunidadResponseDTO(
                o.getIdOportunidades(),
                o.getClienteId(),
                o.getAsesorId(),
                o.getDescripcion(),
                o.getValorEstimado(),
                o.getProbabilidad(),
                o.getEtapaOportunidad(),
                o.getEstado(),
                o.esPotencial(),
                o.getFechaEstimadaCierre(),
                o.getFechaCreacion(),
                o.getFechaActualizacion()
        );
    }

}
