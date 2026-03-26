package com.terreneitors.backendclintec.usuarios.application.port.in;

import com.terreneitors.backendclintec.usuarios.domain.Rol;
import com.terreneitors.backendclintec.usuarios.domain.Usuario;
import com.terreneitors.backendclintec.usuarios.infrastructure.dto.UsuarioRequestDTO;

import java.util.List;
import java.util.Optional;

public interface UsuarioCrudUseCase {
    List<Usuario> findAll();
    Optional<Usuario> buscarPorId(Long id);
    Optional<Usuario> buscarPorEmail(String email);

    Usuario crearUsuario (UsuarioRequestDTO usuario);

    Usuario actualizarUsuario(Long id, UsuarioRequestDTO usuario);

    void desactivarUsuario(String email);
    void activarUsuario(String email);

}
