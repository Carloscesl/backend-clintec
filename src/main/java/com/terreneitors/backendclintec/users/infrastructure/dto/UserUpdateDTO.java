package com.terreneitors.backendclintec.users.infrastructure.dto;

import com.terreneitors.backendclintec.users.domain.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserUpdateDTO(
        @NotBlank String nombre,
        @NotBlank @Email String email,
        String password,  // opcional, sin @NotBlank
        @NotNull Rol rol
) {
}
