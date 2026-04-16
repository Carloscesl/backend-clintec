package com.terreneitors.backendclintec.ventas.application.service;

import com.terreneitors.backendclintec.ventas.application.port.in.ventasCrudUseCase;
import com.terreneitors.backendclintec.ventas.domain.ventas;
import com.terreneitors.backendclintec.ventas.infrastructure.dto.VentaRequestDTO;
import com.terreneitors.backendclintec.ventas.infrastructure.persistence.VentaRepositoryAdapter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class VentaCrudService implements ventasCrudUseCase {

    private final VentaRepositoryAdapter ventaRepositoryAdapter;

    public VentaCrudService(VentaRepositoryAdapter ventaRepositoryAdapter) {
        this.ventaRepositoryAdapter = ventaRepositoryAdapter;
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
    public ventas createVenta(VentaRequestDTO venta) {
        return null;
    }

    @Override
    public ventas updateVenta(Long idVenta, VentaRequestDTO ventaUpdate) {
        return null;
    }
}
