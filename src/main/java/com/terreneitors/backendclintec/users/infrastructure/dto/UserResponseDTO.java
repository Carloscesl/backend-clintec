package com.terreneitors.backendclintec.users.infrastructure.dto;

import com.terreneitors.backendclintec.users.domain.Rol;

public record UserResponseDTO(
        Long id,
        String nombre,
        String email,
        Rol rol,
        boolean activo
){
}
