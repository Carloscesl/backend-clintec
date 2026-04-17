package com.terreneitors.backendclintec.ventas.application.port.in;

import com.terreneitors.backendclintec.ventas.domain.ventas;
import com.terreneitors.backendclintec.ventas.infrastructure.dto.VentaRequestDTO;

import java.util.List;
import java.util.Optional;

public interface ventasCrudUseCase {
    List<ventas> findAll();
    Optional<ventas> findId(Long idVentas);
    Optional<ventas> findIdAsesor(Long idAsesor);
    Optional<ventas> findIdOportunidad(Long idOportunidad);
    ventas createVenta(VentaRequestDTO venta);
    ventas updateVenta(Long idVenta, VentaRequestDTO ventaUpdate);


}
