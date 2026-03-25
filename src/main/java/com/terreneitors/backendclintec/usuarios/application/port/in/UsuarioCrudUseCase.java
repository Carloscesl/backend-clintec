package com.terreneitors.backendclintec.usuarios.application.port.in;

import com.terreneitors.backendclintec.usuarios.domain.Rol;
import com.terreneitors.backendclintec.usuarios.domain.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioCrudUseCase {
    List<Usuario> findAll();
    Usuario crearUsuario (Usuario usuario);
    Optional<Usuario> obtenerUsuario(String email);
    Usuario actualizarUsuario(Long id, Usuario usuario);

    void eliminarUsuario(String email);
    void activarUsuario(String email);

}
