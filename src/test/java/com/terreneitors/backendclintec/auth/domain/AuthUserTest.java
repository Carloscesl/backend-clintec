package com.terreneitors.backendclintec.auth.domain;

import com.terreneitors.backendclintec.users.domain.Rol;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthUserTest {

    @Test
    @DisplayName("Debe instanciar correctamente usando el constructor con argumentos y sus getters")
    void constructorWithArgs_ShouldSetFieldsCorrectly() {
        // Act
        AuthUser user = new AuthUser("carlos@example.com", "securePass123", Rol.ADMINISTRADOR, true);

        // Assert
        assertEquals("carlos@example.com", user.getEmail());
        assertEquals("securePass123", user.getPassword());
        assertEquals(Rol.ADMINISTRADOR, user.getRol());
        assertTrue(user.getActivo());
    }

    @Test
    @DisplayName("Debe asignar y recuperar valores correctamente usando el constructor vacío y setters")
    void settersAndGetters_ShouldWorkCorrectly() {
        // Arrange
        AuthUser user = new AuthUser();

        // Act
        user.setEmail("gerente@example.com");
        user.setPassword("password321");
        user.setRol(Rol.GERENTE);
        user.setActivo(false);

        // Assert
        assertEquals("gerente@example.com", user.getEmail());
        assertEquals("password321", user.getPassword());
        assertEquals(Rol.GERENTE, user.getRol());
        assertFalse(user.getActivo());
    }
}