package com.terreneitors.backendclintec.ventas.infrastructure.persistence;

import com.terreneitors.backendclintec.ventas.application.port.out.VentaRepositoryPort;
import com.terreneitors.backendclintec.ventas.domain.ventas;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class VentaRepositoryAdapter implements VentaRepositoryPort {
    private final SpringVentaRepository ventaRepository;

    public VentaRepositoryAdapter(SpringVentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    public List<ventas> findAll() {
        return List.of();
    }

    @Override
    public Optional<ventas> findId(Long idVentas) {
        return Optional.empty();
    }

    @Override
    public Optional<ventas> findfIdAsesor(Long idAsesor) {
        return Optional.empty();
    }

    @Override
    public Optional<ventas> findfIdOportunidad(Long idOportunidad) {
        return Optional.empty();
    }

    @Override
    public ventas saveVenta(ventas venta) {
        return null;
    }
}
