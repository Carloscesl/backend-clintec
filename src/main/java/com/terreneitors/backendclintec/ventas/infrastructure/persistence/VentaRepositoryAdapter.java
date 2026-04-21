package com.terreneitors.backendclintec.ventas.infrastructure.persistence;

import com.terreneitors.backendclintec.ventas.application.port.out.VentaRepositoryPort;
import com.terreneitors.backendclintec.ventas.domain.Venta;
import com.terreneitors.backendclintec.ventas.infrastructure.persistence.Mapper.VentaPersistenceMapper;
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
    public List<Venta> findAll() {
        return ventaRepository.findAll().stream().map(ventaMapper::toDomain).toList();
    }

    @Override
    public Optional<Venta> findId(Long idVentas) {
        return ventaRepository.findById(idVentas).map(ventaMapper::toDomain);
    }

    @Override
    public List<Venta> findIdAsesor(Long idAsesor) {
        return ventaRepository.findByIdAsesor(idAsesor).stream().map(ventaMapper::toDomain).toList();
    }

    @Override
    public List<Venta> findIdOportunidad(Long idOportunidad) {
        return ventaRepository.findByIdOportunidad(idOportunidad).stream().map(ventaMapper::toDomain).toList();
    }

    @Override
    public Venta saveVenta(Venta venta) {
        return ventaMapper.toDomain(ventaRepository.save(ventaMapper.toEntity(venta)));
    }

}
