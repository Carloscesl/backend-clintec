package com.terreneitors.backendclintec.sales.application.service;

import com.terreneitors.backendclintec.opportunities.application.port.out.OpportunityRepositoryPort;
import com.terreneitors.backendclintec.opportunities.domain.StatusOpportunity;
import com.terreneitors.backendclintec.opportunities.domain.Opportunity;
import com.terreneitors.backendclintec.shared.exception.BusinessException;
import com.terreneitors.backendclintec.shared.exception.InvalidStateException;
import com.terreneitors.backendclintec.shared.exception.ResourceNotFoundException;
import com.terreneitors.backendclintec.sales.application.port.in.SaleCrudUseCase;
import com.terreneitors.backendclintec.sales.application.port.out.SaleRepositoryPort;
import com.terreneitors.backendclintec.sales.domain.Sale;
import com.terreneitors.backendclintec.sales.infrastructure.dto.SaleRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaleCrudService implements SaleCrudUseCase {

    private final SaleRepositoryPort saleRepositoryPort;
    private final OpportunityRepositoryPort oportunidadesRespositoryPort;

    @Override
    public List<Sale> findAll() {
        return saleRepositoryPort.findAll();
    }

    @Override
    public Optional<Sale> findId(Long idVentas) {
        return saleRepositoryPort.findId(idVentas);
    }

    @Override
    public List<Sale> findIdAssessor(Long idAsesor) {
        return saleRepositoryPort.findIdAssessor(idAsesor);
    }

    @Override
    public List<Sale> findIdOpportunity(Long idOportunidad) {
        return saleRepositoryPort.findIdOpportunity(idOportunidad);
    }

    @Override
    public Sale createSale(SaleRequestDTO dto) {
        log.info("[VENTA_CREAR] oportunidadId={} | asesorId={} | valor={}",
                dto.idOportunidad(), dto.idAsesor(), dto.valorVenta());

        Opportunity opportunity = oportunidadesRespositoryPort
                .findById(dto.idOportunidad())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Oportunidad", "id", dto.idOportunidad()));

        if (opportunity.getEstado() != StatusOpportunity.GANADA) {
            log.warn("[VENTA_ESTADO_INVALIDO] oportunidadId={} | estadoActual={}",
                    dto.idOportunidad(), opportunity.getEstado());

            throw new InvalidStateException(
                    "Solo se puede registrar una venta sobre una oportunidad GANADA. " +
                            "Estado actual: " + opportunity.getEstado());
        }

        Sale nuevaSale = new Sale();
        nuevaSale.setIdOportunidad(dto.idOportunidad());
        nuevaSale.setIdAsesor(dto.idAsesor());
        nuevaSale.setValorVenta(dto.valorVenta());
        nuevaSale.setNotas(dto.notas());
        nuevaSale.setMetodoPago(dto.paymentMethod());

        Sale guardada = saleRepositoryPort.saveSale(nuevaSale);
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
    public Sale updateSale(Long idVenta, SaleRequestDTO dto) {

        log.info("[VENTA_ACTUALIZAR] id={}", idVenta);
        Sale sale = saleRepositoryPort.findId(idVenta)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", "id", idVenta));

        sale.setIdOportunidad(dto.idOportunidad());
        sale.setIdAsesor(dto.idAsesor());
        sale.setValorVenta(dto.valorVenta());
        sale.setNotas(dto.notas());
        sale.setMetodoPago(dto.paymentMethod());

        Sale actualizada = saleRepositoryPort.saveSale(sale);
        log.info("[VENTA_ACTUALIZADA] id={} | valor={}", idVenta, actualizada.getValorVenta());

        return actualizada;
    }
}
