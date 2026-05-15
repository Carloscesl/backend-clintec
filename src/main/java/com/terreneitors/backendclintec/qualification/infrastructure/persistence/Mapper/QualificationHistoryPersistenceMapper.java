package com.terreneitors.backendclintec.qualification.infrastructure.persistence.Mapper;

import com.terreneitors.backendclintec.qualification.domain.QualificationHistory;
import com.terreneitors.backendclintec.qualification.infrastructure.dto.QualificationHistoryResponseDTO;
import com.terreneitors.backendclintec.qualification.infrastructure.persistence.QualificationHistoryEntity;
import org.springframework.stereotype.Component;

@Component
public class QualificationHistoryPersistenceMapper {

    public QualificationHistory toDomain(QualificationHistoryEntity e) {
        QualificationHistory d = new QualificationHistory();
        d.setId(e.getId());
        d.setClienteId(e.getClienteId());
        d.setPuntajeAnterior(e.getPuntajeAnterior());
        d.setPuntajeNuevo(e.getPuntajeNuevo());
        d.setMotivo(e.getMotivo());
        d.setFecha(e.getFecha());
        return d;
    }

    public QualificationHistoryEntity toEntity(QualificationHistory d) {
        QualificationHistoryEntity e = new QualificationHistoryEntity();
        e.setId(d.getId());
        e.setClienteId(d.getClienteId());
        e.setPuntajeAnterior(d.getPuntajeAnterior());
        e.setPuntajeNuevo(d.getPuntajeNuevo());
        e.setMotivo(d.getMotivo());
        e.setFecha(d.getFecha());
        return e;
    }

    public QualificationHistoryResponseDTO toHistoryDTO(QualificationHistory h) {
        return new QualificationHistoryResponseDTO(
                h.getId(),
                h.getClienteId(),
                h.getPuntajeAnterior(),
                h.getPuntajeNuevo(),
                h.getMotivo(),
                h.getFecha()
        );
    }
}