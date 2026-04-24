package com.terreneitors.backendclintec.qualification.infrastructure.persistence.Mapper;

import com.terreneitors.backendclintec.qualification.domain.QualificationClient;
import com.terreneitors.backendclintec.qualification.infrastructure.dto.QualificationResponseDTO;
import com.terreneitors.backendclintec.qualification.infrastructure.persistence.QualificationEntity;
import org.springframework.stereotype.Component;

@Component
public class QualificationPersistenceMapper {

    public QualificationClient toDomain(QualificationEntity entity) {
        QualificationClient c = new QualificationClient();
        c.setId(entity.getId());
        c.setClienteId(entity.getClienteId());
        c.setPuntaje(entity.getPuntaje());
        c.setUltimaActualizacion(entity.getUltimaActualizacion());
        return c;
    }

    public QualificationEntity toEntity(QualificationClient c) {
        QualificationEntity entity = new QualificationEntity();
        entity.setId(c.getId());
        entity.setClienteId(c.getClienteId());
        entity.setPuntaje(c.getPuntaje());
        entity.setClasificacion(c.getClasificacion());
        entity.setUltimaActualizacion(c.getUltimaActualizacion());
        return entity;
    }

    public QualificationResponseDTO toDTO(QualificationClient c) {
        return new QualificationResponseDTO(
                c.getId(),
                c.getClienteId(),
                c.getPuntaje(),
                c.getClasificacion(),
                c.getUltimaActualizacion()
        );
    }
}
