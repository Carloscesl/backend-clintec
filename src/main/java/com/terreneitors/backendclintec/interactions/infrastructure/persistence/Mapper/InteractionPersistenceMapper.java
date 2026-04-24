package com.terreneitors.backendclintec.interactions.infrastructure.persistence.Mapper;

import com.terreneitors.backendclintec.interactions.domain.Interaction;
import com.terreneitors.backendclintec.interactions.infrastructure.dto.InteractionResponseDTO;
import com.terreneitors.backendclintec.interactions.infrastructure.persistence.InteractionEntity;
import org.springframework.stereotype.Component;

@Component
public class InteractionPersistenceMapper {
    public Interaction toDomain(InteractionEntity entity) {
        Interaction i = new Interaction();
        i.setId(entity.getId());
        i.setClienteId(entity.getClienteId());
        i.setUsuarioId(entity.getUsuarioId());
        i.setOportunidadId(entity.getOportunidadId());
        i.setTipo(entity.getTipo());
        i.setNota(entity.getNota());
        i.setFecha(entity.getFecha());
        i.setFechaActualizacion(entity.getFechaActualizacion());
        return i;
    }

    public InteractionEntity toEntity(Interaction i) {
        InteractionEntity entity = new InteractionEntity();
        entity.setId(i.getId());
        entity.setClienteId(i.getClienteId());
        entity.setUsuarioId(i.getUsuarioId());
        entity.setOportunidadId(i.getOportunidadId());
        entity.setTipo(i.getTipo());
        entity.setNota(i.getNota());
        entity.setFecha(i.getFecha());
        entity.setFechaActualizacion(i.getFechaActualizacion());
        return entity;
    }

    public InteractionResponseDTO toDTO(Interaction i) {
        return new InteractionResponseDTO(
                i.getId(),
                i.getClienteId(),
                i.getUsuarioId(),
                i.getOportunidadId(),
                i.getTipo(),
                i.getNota(),
                i.getFecha(),
                i.getFechaActualizacion()
        );
    }
}
