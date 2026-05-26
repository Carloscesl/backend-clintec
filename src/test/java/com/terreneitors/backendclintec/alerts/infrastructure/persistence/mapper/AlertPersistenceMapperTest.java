package com.terreneitors.backendclintec.alerts.infrastructure.persistence.mapper;

import com.terreneitors.backendclintec.alerts.domain.Alert;
import com.terreneitors.backendclintec.alerts.domain.StateAlert;
import com.terreneitors.backendclintec.alerts.domain.TypeAlert;
import com.terreneitors.backendclintec.alerts.infrastructure.dto.AlertResponseDTO;
import com.terreneitors.backendclintec.alerts.infrastructure.persistence.AlertEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AlertPersistenceMapperTest {

    private final AlertPersistenceMapper mapper = new AlertPersistenceMapper();

    @Test
    @DisplayName("Debe mapear correctamente de Entity a Domain")
    void toDomain_ShouldMapCorrectly() {
        AlertEntity entity = new AlertEntity();
        entity.setId(1L);
        entity.setDescripcion("Prueba");
        entity.setTipo(TypeAlert.INACTIVIDAD);
        entity.setEstado(StateAlert.PENDIENTE);

        Alert domain = mapper.toDomain(entity);

        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getDescripcion(), domain.getDescripcion());
        assertEquals(entity.getTipo(), domain.getTipo());
        assertEquals(entity.getEstado(), domain.getEstado());
    }

    @Test
    @DisplayName("Debe mapear correctamente de Domain a Entity")
    void toEntity_ShouldMapCorrectly() {
        Alert domain = new Alert();
        domain.setId(1L);
        domain.setDescripcion("Prueba");
        domain.setTipo(TypeAlert.VENCIMIENTO);
        domain.setEstado(StateAlert.RESUELTA);

        AlertEntity entity = mapper.toEntity(domain);

        assertEquals(domain.getId(), entity.getId());
        assertEquals(domain.getDescripcion(), entity.getDescripcion());
        assertEquals(domain.getTipo(), entity.getTipo());
        assertEquals(domain.getEstado(), entity.getEstado());
    }

    @Test
    @DisplayName("Debe mapear correctamente a DTO")
    void toDTO_ShouldMapCorrectly() {
        Alert domain = new Alert();
        domain.setId(1L);
        domain.setDescripcion("DTO Prueba");
        domain.setFechaVencimiento(LocalDateTime.now());

        AlertResponseDTO dto = mapper.toDTO(domain);

        assertEquals(domain.getId(), dto.id());
        assertEquals(domain.getDescripcion(), dto.descripcion());
        assertEquals(domain.getFechaVencimiento(), dto.fechaVencimiento());
    }
}