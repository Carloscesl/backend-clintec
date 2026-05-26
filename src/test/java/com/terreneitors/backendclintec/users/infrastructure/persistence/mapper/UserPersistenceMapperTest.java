package com.terreneitors.backendclintec.users.infrastructure.persistence.mapper;

import com.terreneitors.backendclintec.users.domain.Rol;
import com.terreneitors.backendclintec.users.domain.User;
import com.terreneitors.backendclintec.users.infrastructure.dto.UserResponseDTO;
import com.terreneitors.backendclintec.users.infrastructure.persistence.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserPersistenceMapperTest {

    private final UserPersistenceMapper mapper = new UserPersistenceMapper();

    @Test
    @DisplayName("Debe mapear correctamente Entity a Domain")
    void toDomain_ShouldMapCorrectly() {
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setNombreUser("Carlos");
        entity.setActivo(true);

        User domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(1L, domain.getId());
        assertEquals("Carlos", domain.getNombreUser());
        assertTrue(domain.getActivo());
    }

    @Test
    @DisplayName("Debe retornar null cuando Entity es null")
    void toDomain_ShouldReturnNullWhenInputIsNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    @DisplayName("Debe mapear correctamente de Domain a Entity con Enum")
    void toEntity_ShouldMapCorrectly() {
        User domain = new User();
        domain.setId(2L);
        domain.setRol(Rol.ADMINISTRADOR);
        domain.setActivo(true);

        UserEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(2L, entity.getId());
        assertEquals(Rol.ADMINISTRADOR, entity.getRol());
    }

    @Test
    @DisplayName("Debe mapear correctamente a DTO")
    void toDTO_ShouldMapCorrectly() {
        User domain = new User();
        domain.setId(3L);
        domain.setNombreUser("Ana");
        domain.setRol(Rol.ADMINISTRADOR);
        domain.setActivo(true);

        UserResponseDTO dto = mapper.toDTO(domain);

        assertNotNull(dto);
        assertEquals(3L, dto.id());
        assertEquals("Ana", dto.nombre());
        assertEquals(Rol.ADMINISTRADOR, dto.rol());
        assertTrue(dto.activo());
    }

    @Test
    @DisplayName("Debe retornar null cuando User es null en toDTO")
    void toDTO_ShouldReturnNullWhenInputIsNull() {
        assertNull(mapper.toDTO(null));
    }
}