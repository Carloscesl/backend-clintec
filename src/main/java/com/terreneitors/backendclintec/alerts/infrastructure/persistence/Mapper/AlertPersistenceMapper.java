package com.terreneitors.backendclintec.alerts.infrastructure.persistence.Mapper;

import com.terreneitors.backendclintec.alerts.domain.Alert;
import com.terreneitors.backendclintec.alerts.infrastructure.dto.AlertResponseDTO;
import com.terreneitors.backendclintec.alerts.infrastructure.persistence.AlertEntity;
import org.springframework.stereotype.Component;

@Component
public class AlertPersistenceMapper {
    public Alert toDomain(AlertEntity entity) {
        Alert a = new Alert();
        a.setId(entity.getId());
        a.setClienteId(entity.getClienteId());
        a.setUsuarioId(entity.getUsuarioId());
        a.setDescripcion(entity.getDescripcion());
        a.setTipo(entity.getTipo());
        a.setEstado(entity.getEstado());
        a.setFechaVencimiento(entity.getFechaVencimiento());
        a.setFecha(entity.getFecha());
        a.setFechaActualizacion(entity.getFechaActualizacion());
        return a;
    }

    public AlertEntity toEntity(Alert a) {
        AlertEntity entity = new AlertEntity();
        entity.setId(a.getId());
        entity.setClienteId(a.getClienteId());
        entity.setUsuarioId(a.getUsuarioId());
        entity.setDescripcion(a.getDescripcion());
        entity.setTipo(a.getTipo());
        entity.setEstado(a.getEstado());
        entity.setFechaVencimiento(a.getFechaVencimiento());
        entity.setFecha(a.getFecha());
        entity.setFechaActualizacion(a.getFechaActualizacion());
        return entity;
    }

    public AlertResponseDTO toDTO(Alert a) {
        return new AlertResponseDTO(
                a.getId(),
                a.getClienteId(),
                a.getUsuarioId(),
                a.getDescripcion(),
                a.getTipo(),
                a.getEstado(),
                a.getFechaVencimiento(),
                a.getFecha(),
                a.getFechaActualizacion()
        );
    }
}
