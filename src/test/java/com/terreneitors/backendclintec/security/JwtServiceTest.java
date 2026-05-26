package com.terreneitors.backendclintec.security;

import com.terreneitors.backendclintec.users.domain.Rol;
import com.terreneitors.backendclintec.users.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    private JwtService jwtService;
    private final String secretKey = "esta-es-una-llave-secreta-de-al-menos-32-caracteres-para-jwt";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        // Inyectamos el valor de la propiedad manualmente para el test
        ReflectionTestUtils.setField(jwtService, "SECRET", secretKey);
    }

    @Test
    @DisplayName("Debe generar un token válido y extraer el email")
    void generateToken_ShouldReturnValidToken() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setRol(Rol.ADMINISTRADOR);

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertEquals("test@example.com", jwtService.extractUsername(token));
    }

    @Test
    @DisplayName("Debe validar correctamente un token con el UserDetails")
    void isTokenValid_ShouldReturnTrue_WhenTokenIsValid() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setRol(Rol.ADMINISTRADOR);

        String token = jwtService.generateToken(user);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@example.com");

        assertTrue(jwtService.isTokenValid(token, userDetails));
    }
}