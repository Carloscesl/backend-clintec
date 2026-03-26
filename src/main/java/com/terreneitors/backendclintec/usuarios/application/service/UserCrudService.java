package com.terreneitors.backendclintec.usuarios.application.service;

import com.terreneitors.backendclintec.shared.exception.ResourceNotFoundException;
import com.terreneitors.backendclintec.usuarios.application.port.in.UsuarioCrudUseCase;
import com.terreneitors.backendclintec.usuarios.application.port.out.UsuarioRepositoryPort;
import com.terreneitors.backendclintec.usuarios.domain.Usuario;
import com.terreneitors.backendclintec.usuarios.infrastructure.dto.UsuarioRequestDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserCrudService implements UsuarioCrudUseCase {
    private final UsuarioRepositoryPort usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UserCrudService(UsuarioRepositoryPort usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder= passwordEncoder;
    }

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }
    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }
    @Override
    public Optional<Usuario> obtenerUsuario(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public Usuario crearUsuario(UsuarioRequestDTO dto) {

        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }
        Usuario nuevo = new Usuario();
        nuevo.setNombreUser(dto.nombre());
        nuevo.setEmail(dto.email());
        nuevo.setRol(dto.rol());
        nuevo.setActivo(true);
        nuevo.setFechaCreacion(LocalDateTime.now());

        nuevo.setPassword(passwordEncoder.encode(dto.password()));
        return usuarioRepository.save(nuevo);
    }


    @Override
    public Usuario actualizarUsuario(Long id, UsuarioRequestDTO dto) {
        return usuarioRepository.findById(id).map(u -> {
            u.setNombreUser(dto.nombre());
            u.setEmail(dto.email());
            u.setRol(dto.rol());
            if (dto.password() != null && !dto.password().isBlank()) {
                u.setPassword(passwordEncoder.encode(dto.password()));
            }
            return usuarioRepository.save(u);
        }).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    @Override
    public void desactivarUsuario(String email) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);

        if(usuarioOptional.isEmpty()){
            throw new RuntimeException("Usuario no encontrado");
        }

        Usuario usuario =  usuarioOptional.get();
        usuario.setActivo(false);
        usuarioRepository.save(usuario);

    }

    @Override
    public void activarUsuario(String email) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);

        if(usuarioOptional.isEmpty()){
            throw new RuntimeException("Usuario no encontrado");
        }

        Usuario usuario =  usuarioOptional.get();
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
    }
}
