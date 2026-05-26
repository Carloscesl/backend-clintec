package com.terreneitors.backendclintec.clients.infrastructure.persistence.mapper;

import com.terreneitors.backendclintec.clients.domain.Client;
import com.terreneitors.backendclintec.clients.infrastructure.dto.ClientResponseDTO;
import com.terreneitors.backendclintec.clients.infrastructure.persistence.ClientEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ClientPersistenceMapperTest {

    private final ClientPersistenceMapper mapper = new ClientPersistenceMapper();

    @Test
    @DisplayName("Debe mapear correctamente de Entity a Domain")
    void toDomain_ShouldMapCorrectly() {
        ClientEntity entity = new ClientEntity();
        entity.setId(1L);
        entity.setNombreCliente("Carlos");
        entity.setEmail("carlos@test.com");

        Client domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getNombreCliente(), domain.getNombreCliente());
        assertEquals(entity.getEmail(), domain.getEmail());
    }

    @Test
    @DisplayName("Debe retornar null cuando Entity es null")
    void toDomain_ShouldReturnNullWhenInputIsNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    @DisplayName("Debe mapear correctamente de Domain a Entity")
    void toEntity_ShouldMapCorrectly() {
        Client domain = new Client();
        domain.setId(2L);
        domain.setNombreCliente("Empresa X");

        ClientEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(domain.getId(), entity.getId());
        assertEquals(domain.getNombreCliente(), entity.getNombreCliente());
    }

    @Test
    @DisplayName("Debe retornar null cuando Domain es null")
    void toEntity_ShouldReturnNullWhenInputIsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    @DisplayName("Debe mapear correctamente de Domain a DTO")
    void toDTO_ShouldMapCorrectly() {
        Client domain = new Client();
        domain.setId(3L);
        domain.setNombreCliente("Juan");
        domain.setFechaRegistro(LocalDateTime.now());

        ClientResponseDTO dto = mapper.toDTO(domain);

        assertNotNull(dto);
        assertEquals(domain.getId(), dto.id());
        assertEquals(domain.getNombreCliente(), dto.nombreCliente());
    }

    @Test
    @DisplayName("Debe retornar null cuando DTO input es null")
    void toDTO_ShouldReturnNullWhenInputIsNull() {
        assertNull(mapper.toDTO(null));
    }
}