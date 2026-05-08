package com.terreneitors.backendclintec.users.application.service;

import com.terreneitors.backendclintec.users.application.port.out.UserRepositoryPort;
import com.terreneitors.backendclintec.users.domain.Rol;
import com.terreneitors.backendclintec.users.domain.User;
import com.terreneitors.backendclintec.users.infrastructure.dto.UserRequestDTO;
import com.terreneitors.backendclintec.users.infrastructure.dto.UserUpdateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCrudServiceTest {

    @Mock
    private UserRepositoryPort usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserCrudService userCrudService;

    private User usuarioExistente;
    private UserRequestDTO requestDTO;
    private UserUpdateDTO updateDTO;


    @BeforeEach
    void setUp() {
        usuarioExistente = new User();
        usuarioExistente.setId(1L);
        usuarioExistente.setNombreUser("Carlos López");
        usuarioExistente.setEmail("carlos@mail.com");
        usuarioExistente.setRol(Rol.ADMINISTRADOR);
        usuarioExistente.setActivo(true);
        usuarioExistente.setFechaCreacion(LocalDateTime.now());
        usuarioExistente.setPassword("hashed_password");

        requestDTO = new UserRequestDTO(
                "Carlos López",
                "carlos@mail.com",
                "password123",
                Rol.ADMINISTRADOR
        );

        updateDTO = new UserUpdateDTO(
                "Carlos Actualizado",
                "carlos@mail.com",
                null,
                Rol.ASESOR
        );
    }

    @Test
    void findAll() {
        when(usuarioRepository.findAll())
                .thenReturn(List.of(usuarioExistente));

        List<User> resultado = userCrudService.findAll();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEmail()).isEqualTo("carlos@mail.com");
    }

    @Test
    void findById() {
        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuarioExistente));

        Optional<User> resultado = userCrudService.findById(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(1L);
    }

    @Test
    void findByEmail() {
        when(usuarioRepository.findByEmail("carlos@mail.com"))
                .thenReturn(Optional.of(usuarioExistente));

        Optional<User> resultado = userCrudService.findByEmail("carlos@mail.com");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getEmail()).isEqualTo("carlos@mail.com");
    }

    @Test
    void createUser() {
        when(usuarioRepository.findByEmail("carlos@mail.com"))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123"))
                .thenReturn("hashed_password");
        when(usuarioRepository.save(any(User.class)))
                .thenReturn(usuarioExistente);

        User resultado = userCrudService.createUser(requestDTO);

        assertThat(resultado.getEmail()).isEqualTo("carlos@mail.com");
        assertThat(resultado.getRol()).isEqualTo(Rol.ADMINISTRADOR);
        verify(usuarioRepository).save(any(User.class));
        verify(passwordEncoder).encode("password123");
    }

    @Test
    void updateUser() {
        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(User.class)))
                .thenReturn(usuarioExistente);

        User resultado = userCrudService.updateUser(1L, updateDTO);

        assertThat(resultado).isNotNull();
        verify(usuarioRepository).save(any(User.class));

        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void desactivateUser() {
        when(usuarioRepository.findByEmail("carlos@mail.com"))
                .thenReturn(Optional.of(usuarioExistente));

        userCrudService.desactivateUser("carlos@mail.com");

        assertThat(usuarioExistente.getActivo()).isFalse();
        verify(usuarioRepository).save(usuarioExistente);
    }

    @Test
    void activateUser() {
        usuarioExistente.setActivo(false); // empieza desactivado

        when(usuarioRepository.findByEmail("carlos@mail.com"))
                .thenReturn(Optional.of(usuarioExistente));

        userCrudService.activateUser("carlos@mail.com");

        assertThat(usuarioExistente.getActivo()).isTrue();
        verify(usuarioRepository).save(usuarioExistente);
    }

    @Test
    void count() {
        when(usuarioRepository.count()).thenReturn(10L);

        Long resultado = userCrudService.count();

        assertThat(resultado).isEqualTo(10L);
    }

    @Test
    void countByRol() {
        when(usuarioRepository.countByRol(Rol.ASESOR)).thenReturn(3L);

        Long resultado = userCrudService.countByRol(Rol.ASESOR);

        assertThat(resultado).isEqualTo(3L);
    }
}