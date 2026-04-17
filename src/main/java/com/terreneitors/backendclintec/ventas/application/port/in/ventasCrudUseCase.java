package com.terreneitors.backendclintec.ventas.application.port.in;

import com.terreneitors.backendclintec.ventas.domain.Venta;
import com.terreneitors.backendclintec.ventas.infrastructure.dto.VentaRequestDTO;

import java.util.List;
import java.util.Optional;

public interface ventasCrudUseCase {
    List<Venta> findAll();
    Optional<Venta> findId(Long idVentas);
    List<Venta> findIdAsesor(Long idAsesor);
    List<Venta> findIdOportunidad(Long idOportunidad);
    Venta createVenta(VentaRequestDTO venta);
    Venta updateVenta(Long idVenta, VentaRequestDTO ventaUpdate);
}
