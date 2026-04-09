package com.terreneitors.backendclintec.oportunidades.infrastructure.persistence.mapper;

import com.terreneitors.backendclintec.oportunidades.domain.Oportunidades;
import com.terreneitors.backendclintec.oportunidades.infrastructure.dto.OportunidadesResponseDTO;
import com.terreneitors.backendclintec.oportunidades.infrastructure.persistence.OportunidadesEntity;
import org.springframework.stereotype.Component;

@Component
public class OportunidadesPersistenceMapper {
    public Oportunidades toDomain(OportunidadesEntity entity){
        if(entity==null) return null;
        Oportunidades oportunidad = new Oportunidades();
        oportunidad.setIdOportunidades(entity.getId());
        oportunidad.setAsesorId(entity.getAsesorId());
        oportunidad.setClienteId(entity.getClienteId());
        oportunidad.setDescripcion(entity.getDescripcion());
        oportunidad.setValorEstimado(entity.getValorEstimado());
        oportunidad.setEstado(entity.getEstado());
        oportunidad.setFechaCreacion(entity.getFechaCreacion());
        return oportunidad;
    }
    public OportunidadesEntity toEntity(Oportunidades oportunidad){
        if(oportunidad==null) return null;
        OportunidadesEntity entity = new OportunidadesEntity();
        entity.setId(oportunidad.getIdOportunidades());
        entity.setAsesorId(oportunidad.getAsesorId());
        entity.setClienteId(oportunidad.getClienteId());
        entity.setDescripcion(oportunidad.getDescripcion());
        entity.setValorEstimado(oportunidad.getValorEstimado());
        entity.setEstado(oportunidad.getEstado());
        entity.setFechaCreacion(oportunidad.getFechaCreacion());
        return entity;
    }
    public OportunidadesResponseDTO toDTO(Oportunidades oportunidad){
        if(oportunidad==null) return null;
        return new OportunidadesResponseDTO(
                oportunidad.getIdOportunidades(),
                oportunidad.getClienteId(),
                oportunidad.getAsesorId(),
                oportunidad.getDescripcion(),
                oportunidad.getValorEstimado(),
                oportunidad.getDescripcion(),
                oportunidad.getFechaCreacion()
        );
    }

}
