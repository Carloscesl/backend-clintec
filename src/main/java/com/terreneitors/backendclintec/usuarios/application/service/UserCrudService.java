package com.terreneitors.backendclintec.usuarios.application.service;

import com.terreneitors.backendclintec.shared.exception.ResourceNotFoundException;
import com.terreneitors.backendclintec.shared.exception.ValidationException;
import com.terreneitors.backendclintec.usuarios.application.port.in.UsuarioCrudUseCase;
import com.terreneitors.backendclintec.usuarios.application.port.out.UsuarioRepositoryPort;
import com.terreneitors.backendclintec.usuarios.domain.Usuario;
import com.terreneitors.backendclintec.usuarios.infrastructure.dto.UsuarioRequestDTO;
import com.terreneitors.backendclintec.usuarios.infrastructure.dto.UsuarioUpdateDTO;
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
public class UserCrudService implements UsuarioCrudUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }
    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public Usuario crearUsuario(UsuarioRequestDTO dto) {
         log.info("[USUARIO_CREAR] email={} | rol={}", dto.email(), dto.rol());

        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            log.warn("[USUARIO_EMAIL_DUPLICADO] email={}", dto.email());
            throw new ValidationException(
                    "El correo ya está registrado: " + dto.email());
        }

        Usuario nuevo = new Usuario();
        nuevo.setNombreUser(dto.nombre());
        nuevo.setEmail(dto.email());
        nuevo.setRol(dto.rol());
        nuevo.setActivo(true);
        nuevo.setFechaCreacion(LocalDateTime.now());
        nuevo.setPassword(passwordEncoder.encode(dto.password()));

        Usuario guardado = usuarioRepository.save(nuevo);
        log.info("[USUARIO_CREADO] id={} | email={} | rol={}",
                guardado.getId(), guardado.getEmail(), guardado.getRol());

        return guardado;
    }


    @Override
    public Usuario actualizarUsuario(Long id, UsuarioUpdateDTO dto) {

        log.info("[USUARIO_ACTUALIZAR] id={}", id);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));

        usuario.setNombreUser(dto.nombre());
        usuario.setEmail(dto.email());
        usuario.setRol(dto.rol());

        if (dto.password() != null && !dto.password().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(dto.password()));
            log.info("[USUARIO_PASSWORD_ACTUALIZADO] id={}", id);
        }

        Usuario actualizado = usuarioRepository.save(usuario);
        log.info("[USUARIO_ACTUALIZADO] id={} | email={}", id, actualizado.getEmail());

        return actualizado;
    }

    @Override
    public void desactivarUsuario(String email) {
        log.info("[USUARIO_DESACTIVAR] email={}", email);
        Usuario usuario = buscarPorEmailOFallar(email);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
        log.info("[USUARIO_DESACTIVADO] email={}", email);

    }

    @Override
    public void activarUsuario(String email) {
        log.info("[USUARIO_ACTIVAR] email={}", email);
        Usuario usuario = buscarPorEmailOFallar(email);
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
        log.info("[USUARIO_ACTIVADO] email={}", email);
    }

    private Usuario buscarPorEmailOFallar(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", email));
    }
}
