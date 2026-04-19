package com.terreneitors.backendclintec.ventas.application.service;

import com.terreneitors.backendclintec.oportunidades.application.port.out.OportunidadRespositoryPort;
import com.terreneitors.backendclintec.oportunidades.domain.EstadoOportunidad;
import com.terreneitors.backendclintec.oportunidades.domain.Oportunidad;
import com.terreneitors.backendclintec.shared.exception.BusinessException;
import com.terreneitors.backendclintec.shared.exception.InvalidStateException;
import com.terreneitors.backendclintec.shared.exception.ResourceNotFoundException;
import com.terreneitors.backendclintec.ventas.application.port.in.ventasCrudUseCase;
import com.terreneitors.backendclintec.ventas.application.port.out.VentaRepositoryPort;
import com.terreneitors.backendclintec.ventas.domain.Venta;
import com.terreneitors.backendclintec.ventas.infrastructure.dto.VentaRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VentaCrudService implements ventasCrudUseCase {

    private final VentaRepositoryPort ventaRepositoryPort;
    private final OportunidadRespositoryPort oportunidadesRespositoryPort;

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
        log.info("[VENTA_CREAR] oportunidadId={} | asesorId={} | valor={}",
                dto.idOportunidad(), dto.idAsesor(), dto.valorVenta());

        Oportunidad oportunidad = oportunidadesRespositoryPort
                .findById(dto.idOportunidad())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Oportunidad", "id", dto.idOportunidad()));

        if (oportunidad.getEstado() != EstadoOportunidad.GANADA) {
            log.warn("[VENTA_ESTADO_INVALIDO] oportunidadId={} | estadoActual={}",
                    dto.idOportunidad(), oportunidad.getEstado());

            throw new InvalidStateException(
                    "Solo se puede registrar una venta sobre una oportunidad GANADA. " +
                            "Estado actual: " + oportunidad.getEstado());
        }

        Venta nuevaVenta = new Venta();
        nuevaVenta.setIdOportunidad(dto.idOportunidad());
        nuevaVenta.setIdAsesor(dto.idAsesor());
        nuevaVenta.setValorVenta(dto.valorVenta());
        nuevaVenta.setNotas(dto.notas());
        nuevaVenta.setMetodoPago(dto.metodoPago());

        Venta guardada = ventaRepositoryPort.saveVenta(nuevaVenta);
        if (guardada == null || guardada.getIdVenta() == null) {
            log.error("[VENTA_CREAR_FALLIDO] oportunidadId={}", dto.idOportunidad());
            throw new BusinessException("ERROR_CREAR_VENTA",
                    "No se pudo registrar la venta. Intenta de nuevo.");
        }

        log.info("[VENTA_CREADA] id={} | oportunidadId={} | valor={}",
                guardada.getIdVenta(), guardada.getIdOportunidad(), guardada.getValorVenta());

        return guardada;
    }

    @Override
    public Venta updateVenta(Long idVenta, VentaRequestDTO dto) {

        log.info("[VENTA_ACTUALIZAR] id={}", idVenta);
        Venta venta = ventaRepositoryPort.findId(idVenta)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", "id", idVenta));

        venta.setIdOportunidad(dto.idOportunidad());
        venta.setIdAsesor(dto.idAsesor());
        venta.setValorVenta(dto.valorVenta());
        venta.setNotas(dto.notas());
        venta.setMetodoPago(dto.metodoPago());

        Venta actualizada = ventaRepositoryPort.saveVenta(venta);
        log.info("[VENTA_ACTUALIZADA] id={} | valor={}", idVenta, actualizada.getValorVenta());

        return actualizada;
    }
}
