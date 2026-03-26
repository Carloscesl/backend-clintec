package com.terreneitors.backendclintec.usuarios.infrastructure.dto;

import com.terreneitors.backendclintec.usuarios.domain.Rol;

public record UsuarioResponseDTO (
        Long id,
        String nombre,
        String email,
        Rol rol,
        boolean activo
){
}
