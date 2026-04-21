package com.terreneitors.backendclintec.ventas.application.port.out;

import com.terreneitors.backendclintec.ventas.domain.Venta;

import java.util.List;
import java.util.Optional;

public interface VentaRepositoryPort {
    List<Venta> findAll();
    Optional<Venta> findId(Long idVentas);
    List<Venta> findIdAsesor(Long idAsesor);
    List<Venta> findIdOportunidad(Long idOportunidad);
    Venta saveVenta(Venta venta);
}
