package com.terreneitors.backendclintec.users.application.service;

import com.terreneitors.backendclintec.shared.exception.ResourceNotFoundException;
import com.terreneitors.backendclintec.shared.exception.ValidationException;
import com.terreneitors.backendclintec.users.application.port.in.UserCrudUseCase;
import com.terreneitors.backendclintec.users.application.port.out.UserRepositoryPort;
import com.terreneitors.backendclintec.users.domain.User;
import com.terreneitors.backendclintec.users.infrastructure.dto.UserRequestDTO;
import com.terreneitors.backendclintec.users.infrastructure.dto.UserUpdateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCrudService implements UserCrudUseCase {

    private final UserRepositoryPort usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll() {
        return usuarioRepository.findAll();
    }
    @Override
    public Optional<User> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public User createUser(UserRequestDTO dto) {
         log.info("[USUARIO_CREAR] email={} | rol={}", dto.email(), dto.rol());

        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            log.warn("[USUARIO_EMAIL_DUPLICADO] email={}", dto.email());
            throw new ValidationException(
                    "El correo ya está registrado: " + dto.email());
        }

        User nuevo = new User();
        nuevo.setNombreUser(dto.nombre());
        nuevo.setEmail(dto.email());
        nuevo.setRol(dto.rol());
        nuevo.setActivo(true);
        nuevo.setFechaCreacion(LocalDateTime.now());
        nuevo.setPassword(passwordEncoder.encode(dto.password()));

        User guardado = usuarioRepository.save(nuevo);
        log.info("[USUARIO_CREADO] id={} | email={} | rol={}",
                guardado.getId(), guardado.getEmail(), guardado.getRol());

        return guardado;
    }


    @Override
    public User updateUser(Long id, UserUpdateDTO dto) {

        log.info("[USUARIO_ACTUALIZAR] id={}", id);

        User user = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));

        user.setNombreUser(dto.nombre());
        user.setEmail(dto.email());
        user.setRol(dto.rol());

        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
            log.info("[USUARIO_PASSWORD_ACTUALIZADO] id={}", id);
        }

        User actualizado = usuarioRepository.save(user);
        log.info("[USUARIO_ACTUALIZADO] id={} | email={}", id, actualizado.getEmail());

        return actualizado;
    }

    @Override
    public void desactivateUser(String email) {
        log.info("[USUARIO_DESACTIVAR] email={}", email);
        User user = buscarPorEmailOFallar(email);
        user.setActivo(false);
        usuarioRepository.save(user);
        log.info("[USUARIO_DESACTIVADO] email={}", email);

    }

    @Override
    public void activateUser(String email) {
        log.info("[USUARIO_ACTIVAR] email={}", email);
        User user = buscarPorEmailOFallar(email);
        user.setActivo(true);
        usuarioRepository.save(user);
        log.info("[USUARIO_ACTIVADO] email={}", email);
    }

    private User buscarPorEmailOFallar(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", email));
    }
}
