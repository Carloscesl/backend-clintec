package com.terreneitors.backendclintec.usuarios.infrastructure.dto;

import com.terreneitors.backendclintec.usuarios.domain.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioUpdateDTO(
        @NotBlank String nombre,
        @NotBlank @Email String email,
        String password,  // opcional, sin @NotBlank
        @NotNull Rol rol
) {
}
