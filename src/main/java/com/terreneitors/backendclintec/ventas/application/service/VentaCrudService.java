package com.terreneitors.backendclintec.ventas.application.service;

import com.terreneitors.backendclintec.oportunidades.application.port.out.OportunidadesRespositoryPort;
import com.terreneitors.backendclintec.oportunidades.domain.EstadoOportunidad;
import com.terreneitors.backendclintec.oportunidades.domain.Oportunidad;
import com.terreneitors.backendclintec.shared.exception.ResourceNotFoundException;
import com.terreneitors.backendclintec.ventas.application.port.in.ventasCrudUseCase;
import com.terreneitors.backendclintec.ventas.application.port.out.VentaRepositoryPort;
import com.terreneitors.backendclintec.ventas.domain.Venta;
import com.terreneitors.backendclintec.ventas.infrastructure.dto.VentaRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class VentaCrudService implements ventasCrudUseCase {

    private final VentaRepositoryPort ventaRepositoryPort;
    private final OportunidadesRespositoryPort oportunidadesRespositoryPort;


    @Override
    public List<Venta> findAll() {
        return ventaRepositoryPort.findAll();
    }

    @Override
    public Optional<Venta> findId(Long idVentas) {
        return ventaRepositoryPort.findId(idVentas);
    }

    @Override
    public List<Venta> findIdAsesor(Long idAsesor) {
        return ventaRepositoryPort.findIdAsesor(idAsesor);
    }

    @Override
    public List<Venta> findIdOportunidad(Long idOportunidad) {
        return ventaRepositoryPort.findIdOportunidad(idOportunidad);
    }

    @Override
    public Venta createVenta(VentaRequestDTO dto) {

        Oportunidad oportunidad = oportunidadesRespositoryPort.findById(dto.idOportunidad())
                .orElseThrow(() -> new RuntimeException( "Oportunidad no encontrada con id: " + dto.idOportunidad()));

        if (oportunidad.getEstado() != EstadoOportunidad.GANADA)
            throw new IllegalStateException(
                    "Solo se puede registrar una venta sobre una oportunidad GANADA. " +
                            "Estado actual: " + oportunidad.getEstado());

        Venta nuevaVenta = new Venta();
        nuevaVenta.setIdOportunidad(dto.idOportunidad());
        nuevaVenta.setIdAsesor(dto.idAsesor());
        nuevaVenta.setValorVenta(dto.valorVenta());
        nuevaVenta.setNotas(dto.notas());
        nuevaVenta.setMetodoPago(dto.metodoPago());
        nuevaVenta.setFechaVenta(LocalDateTime.now());
        nuevaVenta.setFechaActualizacion(LocalDateTime.now());
        return ventaRepositoryPort.saveVenta(nuevaVenta);
    }

    @Override
    public Venta updateVenta(Long idVenta, VentaRequestDTO ventaUpdate) {

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
