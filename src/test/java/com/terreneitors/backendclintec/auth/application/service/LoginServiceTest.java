package com.terreneitors.backendclintec.auth.application.service;

import com.terreneitors.backendclintec.auth.infrastructure.dto.TokenResponse;
import com.terreneitors.backendclintec.security.JwtService;
import com.terreneitors.backendclintec.shared.exception.InvalidStateException;
import com.terreneitors.backendclintec.shared.exception.ResourceNotFoundException;
import com.terreneitors.backendclintec.shared.exception.ValidationException;
import com.terreneitors.backendclintec.users.application.port.out.UserRepositoryPort;
import com.terreneitors.backendclintec.users.domain.Rol;
import com.terreneitors.backendclintec.users.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private UserRepositoryPort usuarioRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginService loginService;

    @Test
    @DisplayName("Debería iniciar sesión exitosamente con credenciales válidas")
    void login_Success() {
        // 1. Arrange
        String email = "asesor@clintec.com";
        String passRaw = "clave123";
        String passEncrypted = "$2a$10$encrypted";
        String tokenSimulado = "jwt.token.mock";

        User user = new User();
        user.setId(10L);
        user.setNombreUser("Fabiana");
        user.setEmail(email);
        user.setPassword(passEncrypted);
        user.setRol(Rol.ASESOR);
        user.setActivo(true);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(passRaw, passEncrypted)).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn(tokenSimulado);

        // 2. Act
        TokenResponse response = loginService.login(email, passRaw);

        // 3. Assert
        assertNotNull(response);
        assertEquals(user.getId(), response.id());
        assertEquals(user.getNombreUser(), response.username());
        assertEquals(email, response.email());
        assertEquals(tokenSimulado, response.token());
        assertTrue(response.roles().contains("ASESOR"));

        verify(usuarioRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(passRaw, passEncrypted);
        verify(jwtService, times(1)).generateToken(user);
    }

    @Test
    @DisplayName("Debería lanzar ResourceNotFoundException si el email no existe")
    void login_ThrowsResourceNotFoundException_WhenUserDoesNotExist() {
        // 1. Arrange
        String email = "noexiste@clintec.com";
        String pass = "password";

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        // 2. Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            loginService.login(email, pass);
        });

        // Aseguramos que el flujo se corta de inmediato
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtService, never()).generateToken(any(User.class));
    }

    @Test
    @DisplayName("Debería lanzar InvalidStateException si el usuario está inactivo")
    void login_ThrowsInvalidStateException_WhenUserIsInactive() {
        // 1. Arrange
        String email = "inactivo@clintec.com";
        String pass = "password";

        User userInactivo = new User();
        userInactivo.setEmail(email);
        userInactivo.setActivo(false); // <--- Usuario Desactivado

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(userInactivo));

        // 2. Act & Assert
        InvalidStateException exception = assertThrows(InvalidStateException.class, () -> {
            loginService.login(email, pass);
        });

        assertEquals("La cuenta está desactivada. Contacta al administrador.", exception.getMessage());

        // El flujo se detiene antes de comprobar contraseña o generar token
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtService, never()).generateToken(any(User.class));
    }

    @Test
    @DisplayName("Debería lanzar ValidationException si la contraseña no coincide")
    void login_ThrowsValidationException_WhenPasswordDoesNotMatch() {
        // 1. Arrange
        String email = "admin@clintec.com";
        String passIncorrecta = "claveErronea";
        String passEncrypted = "$2a$10$hashReal";

        User user = new User();
        user.setEmail(email);
        user.setPassword(passEncrypted);
        user.setActivo(true);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(user));
        // Forzamos a que el encoder diga que no coinciden las claves
        when(passwordEncoder.matches(passIncorrecta, passEncrypted)).thenReturn(false);

        // 2. Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            loginService.login(email, passIncorrecta);
        });

        assertEquals("Email o contraseña incorrectos.", exception.getMessage());

        // Verifica que se validó la clave pero NUNCA se generó el token
        verify(passwordEncoder, times(1)).matches(passIncorrecta, passEncrypted);
        verify(jwtService, never()).generateToken(any(User.class));
    }
}