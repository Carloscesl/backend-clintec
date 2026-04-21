package com.terreneitors.backendclintec.sales.application.port.out;

import com.terreneitors.backendclintec.sales.domain.Sale;

import java.util.List;
import java.util.Optional;

public interface SaleRepositoryPort {
    List<Sale> findAll();
    Optional<Sale> findId(Long idVentaLong);
    List<Sale> findIdAssessor(Long idAsesor);
    List<Sale> findIdOpportunity(Long idOportunidad);
    Sale saveSale(Sale sale);
}
