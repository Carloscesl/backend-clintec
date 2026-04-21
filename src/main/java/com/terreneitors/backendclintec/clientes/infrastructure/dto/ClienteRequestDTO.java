package com.terreneitors.backendclintec.clientes.infrastructure.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ClienteRequestDTO(

        @NotBlank(message = "El nombre del cliente no puede estar vacío")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        String nombreCliente,

        @NotBlank(message = "El nombre de la empresa no puede estar vacío") // ✅ typo corregido
        @Size(min = 2, max = 100, message = "La empresa debe tener entre 2 y 100 caracteres")
        String empresa,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Debe ser un email válido")
        String email,

        @NotBlank(message = "El teléfono no puede estar vacío")
        @Pattern(
                regexp = "^[+]?[0-9]{7,15}$",
                message = "El teléfono debe contener entre 7 y 15 dígitos"
        )
        String telefono,

        @NotBlank(message = "La dirección no puede estar vacía") // ✅ typo corregido
        @Size(max = 200, message = "La dirección no puede superar 200 caracteres")
        String direccion
) {
}
