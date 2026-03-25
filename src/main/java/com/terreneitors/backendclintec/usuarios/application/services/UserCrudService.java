package com.terreneitors.backendclintec.usuarios.application.services;

import com.terreneitors.backendclintec.usuarios.application.port.in.UsuarioCrudUseCase;
import com.terreneitors.backendclintec.usuarios.application.port.out.UsuarioRepositoryPort;
import com.terreneitors.backendclintec.usuarios.domain.Rol;
import com.terreneitors.backendclintec.usuarios.domain.Usuario;
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
    public Usuario crearUsuario(Usuario usuario) {
        return null;
    }

    @Override
    public Optional<Usuario> obtenerUsuario(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public Usuario actualizarUsuario(Long id, Usuario usuario) {
        return null;
    }

    @Override
    public void eliminarUsuario(String email) {
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
