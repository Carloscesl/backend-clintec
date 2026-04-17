package com.terreneitors.backendclintec.ventas.infrastructure.persistence;

import com.terreneitors.backendclintec.ventas.application.port.out.VentaRepositoryPort;
import com.terreneitors.backendclintec.ventas.domain.ventas;
import com.terreneitors.backendclintec.ventas.infrastructure.persistence.mapper.VentaPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VentaRepositoryAdapter implements VentaRepositoryPort {
    private final SpringVentaRepository ventaRepository;
    private final VentaPersistenceMapper ventaMapper;

    @Override
    public List<ventas> findAll() {
        return ventaRepository.findAll().stream().map(ventaMapper::toDomain).toList();
    }

    @Override
    public Optional<ventas> findId(Long idVentas) {
        return ventaRepository.findById(idVentas).map(ventaMapper::toDomain);
    }

    @Override
    public Optional<ventas> findfIdAsesor(Long idAsesor) {
        return ventaRepository.findByIdAsesor(idAsesor).map(ventaMapper::toDomain);
    }

    @Override
    public Optional<ventas> findfIdOportunidad(Long idOportunidad) {
        return ventaRepository.findByIdOportuniad(idOportunidad).map(ventaMapper::toDomain);
    }

    @Override
    public ventas saveVenta(ventas venta) {
        return ventaMapper.toDomain(ventaRepository.save(ventaMapper.toEntity(venta)));
    }
}
