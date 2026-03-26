package com.terreneitors.backendclintec.usuarios.application.port.out;

import com.terreneitors.backendclintec.usuarios.domain.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepositoryPort {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findById(Long id);
    Usuario save(Usuario usuario);
    List<Usuario> findAll();
}
