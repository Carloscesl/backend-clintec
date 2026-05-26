package com.terreneitors.backendclintec.interactions.infrastructure.persistence.Mapper;

import com.terreneitors.backendclintec.interactions.domain.Interaction;
import com.terreneitors.backendclintec.interactions.infrastructure.dto.InteractionResponseDTO;
import com.terreneitors.backendclintec.interactions.infrastructure.persistence.InteractionEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InteractionPersistenceMapperTest {

    private final InteractionPersistenceMapper mapper = new InteractionPersistenceMapper();

    @Test
    @DisplayName("Debe mapear correctamente de Entity a Domain")
    void toDomain_ShouldMapCorrectly() {
        InteractionEntity entity = new InteractionEntity();
        entity.setId(1L);
        entity.setNota("Nota de prueba");

        Interaction domain = mapper.toDomain(entity);

        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getNota(), domain.getNota());
    }

    @Test
    @DisplayName("Debe mapear correctamente de Domain a Entity")
    void toEntity_ShouldMapCorrectly() {
        Interaction domain = new Interaction();
        domain.setId(1L);
        domain.setNota("Nota de prueba");

        InteractionEntity entity = mapper.toEntity(domain);

        assertEquals(domain.getId(), entity.getId());
        assertEquals(domain.getNota(), entity.getNota());
    }

    @Test
    @DisplayName("Debe mapear correctamente a DTO")
    void toDTO_ShouldMapCorrectly() {
        Interaction domain = new Interaction();
        domain.setId(1L);
        domain.setNota("Nota de prueba");
        domain.setFecha(LocalDateTime.now());

        InteractionResponseDTO dto = mapper.toDTO(domain);

        assertEquals(domain.getId(), dto.id());
        assertEquals(domain.getNota(), dto.nota());
        assertEquals(domain.getFecha(), dto.fecha());
    }
}