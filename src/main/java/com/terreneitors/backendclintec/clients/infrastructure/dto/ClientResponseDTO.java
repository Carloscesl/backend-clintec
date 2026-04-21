package com.terreneitors.backendclintec.clients.infrastructure.dto;

import java.time.LocalDateTime;

public record ClientResponseDTO(
        Long id,
        String nombreCliente,
        String empresa,
        String email,
        String telefono,
        String direccion,
        LocalDateTime fechaRegistro
) {
}
