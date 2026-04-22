package com.terreneitors.backendclintec.sales.application.port.in;

import com.terreneitors.backendclintec.sales.domain.Sale;
import com.terreneitors.backendclintec.sales.infrastructure.dto.SaleRequestDTO;

import java.util.List;
import java.util.Optional;

public interface SaleCrudUseCase {
    List<Sale> findAll();
    Optional<Sale> findId(Long idVentas);
    List<Sale> findIdAssessor(Long idAsesor);
    List<Sale> findIdOpportunity(Long idOportunidad);
    Sale createSale(SaleRequestDTO venta);
    Sale updateSale(Long idVenta, SaleRequestDTO ventaUpdate);
}
