package com.terreneitors.backendclintec.qualification.infrastructure.persistence.mapper;

import com.terreneitors.backendclintec.qualification.domain.QualificationHistory;
import com.terreneitors.backendclintec.qualification.infrastructure.dto.QualificationHistoryResponseDTO;
import com.terreneitors.backendclintec.qualification.infrastructure.persistence.QualificationHistoryEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class QualificationHistoryPersistenceMapperTest {

    private final QualificationHistoryPersistenceMapper mapper = new QualificationHistoryPersistenceMapper();

    @Test
    @DisplayName("Debe mapear correctamente Entity a Domain")
    void toDomain_ShouldMapCorrectly() {
        QualificationHistoryEntity entity = new QualificationHistoryEntity();
        entity.setId(1L);
        entity.setMotivo("Actualización manual");
        entity.setPuntajeNuevo(90);

        QualificationHistory domain = mapper.toDomain(entity);

        assertEquals(1L, domain.getId());
        assertEquals("Actualización manual", domain.getMotivo());
        assertEquals(90, domain.getPuntajeNuevo());
    }

    @Test
    @DisplayName("Debe mapear correctamente Domain a Entity")
    void toEntity_ShouldMapCorrectly() {
        QualificationHistory domain = new QualificationHistory();
        domain.setId(2L);
        domain.setMotivo("Cambio de nivel");
        domain.setPuntajeAnterior(80);

        QualificationHistoryEntity entity = mapper.toEntity(domain);

        assertEquals(2L, entity.getId());
        assertEquals("Cambio de nivel", entity.getMotivo());
        assertEquals(80, entity.getPuntajeAnterior());
    }

    @Test
    @DisplayName("Debe mapear correctamente a DTO de historial")
    void toHistoryDTO_ShouldMapCorrectly() {
        QualificationHistory domain = new QualificationHistory();
        domain.setId(3L);
        domain.setFecha(LocalDateTime.now());
        domain.setMotivo("Reevaluación");

        QualificationHistoryResponseDTO dto = mapper.toHistoryDTO(domain);

        assertEquals(3L, dto.id());
        assertEquals("Reevaluación", dto.motivo());
        assertNotNull(dto.fecha());
    }
}