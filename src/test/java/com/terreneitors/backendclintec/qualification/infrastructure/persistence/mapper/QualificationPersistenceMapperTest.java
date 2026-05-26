package com.terreneitors.backendclintec.qualification.infrastructure.persistence.mapper;

import com.terreneitors.backendclintec.qualification.domain.Qualification;
import com.terreneitors.backendclintec.qualification.domain.QualificationClient;
import com.terreneitors.backendclintec.qualification.infrastructure.dto.QualificationResponseDTO;
import com.terreneitors.backendclintec.qualification.infrastructure.persistence.QualificationEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class QualificationPersistenceMapperTest {

    private final QualificationPersistenceMapper mapper = new QualificationPersistenceMapper();

    @Test
    @DisplayName("Debe mapear correctamente de Entity a Domain")
    void toDomain_ShouldMapCorrectly() {
        QualificationEntity entity = new QualificationEntity();
        entity.setId(1L);
        entity.setClienteId(55L);
        entity.setPuntaje(85);

        QualificationClient domain = mapper.toDomain(entity);

        assertEquals(1L, domain.getId());
        assertEquals(55L, domain.getClienteId());
        assertEquals(85, domain.getPuntaje());
    }

    @Test
    @DisplayName("Debe mapear correctamente de Domain a Entity")
    void toEntity_ShouldMapCorrectly() {
        QualificationClient domain = new QualificationClient();
        domain.setId(1L);
        domain.setPuntaje(90);
        // Asegúrate de que tu clase QualificationClient tenga el setter de clasificación si es necesario
        domain.setClasificacion(Qualification.FRIO);

        QualificationEntity entity = mapper.toEntity(domain);

        assertEquals(1L, entity.getId());
        assertEquals(90, entity.getPuntaje());
        assertEquals(Qualification.FRIO, entity.getClasificacion());
    }

    @Test
    @DisplayName("Debe mapear correctamente a DTO")
    void toDTO_ShouldMapCorrectly() {
        QualificationClient domain = new QualificationClient();
        domain.setId(1L);
        domain.setClienteId(55L);
        domain.setPuntaje(95);
        domain.setClasificacion(Qualification.FRIO);
        domain.setUltimaActualizacion(LocalDateTime.now());

        QualificationResponseDTO dto = mapper.toDTO(domain);

        assertEquals(1L, dto.id());
        assertEquals(95, dto.puntaje());
        assertEquals(Qualification.FRIO, dto.clasificacion());
    }
}