package com.terreneitors.backendclintec.clientes.infrastructure.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record ClienteRequestDTO(

        @NotBlank(message = "El nombre no puede estar vacío")
        String nombreCliente,

        @NotBlank(message = "El nombre de la emppresa no puede estar vacío")
        String empresa,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Debe ser un email válido")
        String email,

        @NotBlank(message = "El telefono no puede estar vacío")
        String telefono,

        @NotBlank(message = "La dirreccion no puede estar vacío")
        String direccion
) {
}
