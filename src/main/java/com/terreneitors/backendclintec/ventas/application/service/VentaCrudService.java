package com.terreneitors.backendclintec.ventas.application.service;

import com.terreneitors.backendclintec.shared.exception.ResourceNotFoundException;
import com.terreneitors.backendclintec.ventas.application.port.in.ventasCrudUseCase;
import com.terreneitors.backendclintec.ventas.application.port.out.VentaRepositoryPort;
import com.terreneitors.backendclintec.ventas.domain.ventas;
import com.terreneitors.backendclintec.ventas.infrastructure.dto.VentaRequestDTO;
import com.terreneitors.backendclintec.ventas.infrastructure.persistence.VentaRepositoryAdapter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class VentaCrudService implements ventasCrudUseCase {

    private final VentaRepositoryPort ventaRepositoryPort;

    public VentaCrudService(VentaRepositoryPort ventaRepositoryPort) {
        this.ventaRepositoryPort = ventaRepositoryPort;
    }

    @Override
    public List<ventas> findAll() {
        return ventaRepositoryPort.findAll();
    }

    @Override
    public Optional<ventas> findId(Long idVentas) {
        return ventaRepositoryPort.findId(idVentas);
    }

    @Override
    public Optional<ventas> findIdAsesor(Long idAsesor) {
        return ventaRepositoryPort.findIdAsesor(idAsesor);
    }

    @Override
    public Optional<ventas> findIdOportunidad(Long idOportunidad) {
        return ventaRepositoryPort.findIdOportunidad(idOportunidad);
    }

    @Override
    public ventas createVenta(VentaRequestDTO venta) {

        ventas nuevaVenta = new ventas();
        nuevaVenta.setIdOportunidad(venta.idOportunidad());
        nuevaVenta.setIdAsesor(venta.idAsesor());
        nuevaVenta.setValorVenta(venta.valorVenta());
        nuevaVenta.setNotas(venta.notas());
        nuevaVenta.setMetodoPago(venta.metodoPago());
        nuevaVenta.setFechaVenta(LocalDateTime.now());
        nuevaVenta.setFechaActualizacion(LocalDateTime.now());
        return ventaRepositoryPort.saveVenta(nuevaVenta);
    }

    @Override
    public ventas updateVenta(Long idVenta, VentaRequestDTO ventaUpdate) {

        return ventaRepositoryPort.findId(idVenta).map(u->{
            u.setIdOportunidad(ventaUpdate.idOportunidad());
            u.setIdAsesor(ventaUpdate.idAsesor());
            u.setValorVenta(ventaUpdate.valorVenta());
            u.setNotas(ventaUpdate.notas());
            u.setMetodoPago(ventaUpdate.metodoPago());
            u.setFechaVenta(LocalDateTime.now());
            u.setFechaActualizacion(LocalDateTime.now());
            return ventaRepositoryPort.saveVenta(u);
        }).orElseThrow(()->new ResourceNotFoundException("Venta no encontrada"));
    }
}
