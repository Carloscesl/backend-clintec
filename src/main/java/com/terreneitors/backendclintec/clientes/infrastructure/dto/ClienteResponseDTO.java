package com.terreneitors.backendclintec.clientes.infrastructure.dto;

import java.time.LocalDateTime;

public record ClienteResponseDTO(
        Long id,
        String nombreCliente,
        String empresa,
        String email,
        String telefono,
        String direccion,
        LocalDateTime fechaRegistro
) {
}
