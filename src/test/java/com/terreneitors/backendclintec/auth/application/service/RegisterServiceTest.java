package com.terreneitors.backendclintec.auth.application.service;

import com.terreneitors.backendclintec.auth.infrastructure.dto.TokenResponse;
import com.terreneitors.backendclintec.security.JwtService;
import com.terreneitors.backendclintec.shared.exception.ValidationException;
import com.terreneitors.backendclintec.users.application.port.out.UserRepositoryPort;
import com.terreneitors.backendclintec.users.domain.Rol;
import com.terreneitors.backendclintec.users.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {

    @Mock
    private UserRepositoryPort usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private RegisterService registerService;

    @Test
    @DisplayName("Debería registrar un usuario exitosamente cuando el email no existe")
    void register_Success() {
        // 1. Arrange
        String nombre = "Carlos Sanchez";
        String email = "carlos@terreneitors.com";
        String passRaw = "password123";
        String passEncrypted = "encryptedPassword123";
        String simulatedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...mock";

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(passRaw)).thenReturn(passEncrypted);
        when(jwtService.generateToken(any(User.class))).thenReturn(simulatedToken);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        // 2. Act
        TokenResponse response = registerService.register(nombre, email, passRaw);

        // 3. Assert (Usando los métodos nativos del Record sin 'get')
        assertNotNull(response);
        assertEquals(nombre, response.username());
        assertEquals(email, response.email());
        assertEquals(simulatedToken, response.token());
        assertTrue(response.roles().contains("ADMINISTRADOR"));

        verify(usuarioRepository, times(1)).save(userCaptor.capture());

        User usuarioGuardado = userCaptor.getValue();
        assertEquals(nombre, usuarioGuardado.getNombreUser());
        assertEquals(email, usuarioGuardado.getEmail());
        assertEquals(passEncrypted, usuarioGuardado.getPassword());
        assertEquals(Rol.ADMINISTRADOR, usuarioGuardado.getRol());
        assertTrue(usuarioGuardado.getActivo());
        assertNotNull(usuarioGuardado.getFechaCreacion());
    }

    @Test
    @DisplayName("Debería lanzar ValidationException si el correo electrónico ya se encuentra registrado")
    void register_ThrowsValidationException_WhenEmailExists() {
        // 1. Arrange
        String nombre = "Carlos Sanchez";
        String email = "carlos@terreneitors.com";
        String pass = "password123";

        User usuarioExistente = new User();
        usuarioExistente.setEmail(email);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuarioExistente));

        // 2. Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            registerService.register(nombre, email, pass);
        });

        assertEquals("El correo ya está registrado: " + email, exception.getMessage());

        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, never()).save(any(User.class));
        verify(jwtService, never()).generateToken(any(User.class));
    }
}