package com.terreneitors.backendclintec.ventas.application.port.out;

import com.terreneitors.backendclintec.ventas.application.service.VentaCrudService;
import com.terreneitors.backendclintec.ventas.domain.ventas;
import com.terreneitors.backendclintec.ventas.infrastructure.dto.VentaRequestDTO;

import java.util.List;
import java.util.Optional;

public interface VentaRepositoryPort {
    List<ventas> findAll();
    Optional<ventas> findId(Long idVentas);
    Optional<ventas> findIdAsesor(Long idAsesor);
    Optional<ventas> findIdOportunidad(Long idOportunidad);
    ventas saveVenta(ventas venta);
}
