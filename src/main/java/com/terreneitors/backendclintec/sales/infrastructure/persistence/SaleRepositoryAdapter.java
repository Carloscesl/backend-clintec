package com.terreneitors.backendclintec.sales.infrastructure.persistence;

import com.terreneitors.backendclintec.sales.application.port.out.SaleRepositoryPort;
import com.terreneitors.backendclintec.sales.domain.Sale;
import com.terreneitors.backendclintec.sales.infrastructure.persistence.Mapper.SalePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SaleRepositoryAdapter implements SaleRepositoryPort {
    private final SpringSaleRepository ventaRepository;
    private final SalePersistenceMapper ventaMapper;

    @Override
    public List<Sale> findAll() {
        return ventaRepository.findAll().stream().map(ventaMapper::toDomain).toList();
    }

    @Override
    public Optional<Sale> findId(Long idVentas) {
        return ventaRepository.findById(idVentas).map(ventaMapper::toDomain);
    }

    @Override
    public List<Sale> findIdAssessor(Long idAsesor) {
        return ventaRepository.findByIdAsesor(idAsesor).stream().map(ventaMapper::toDomain).toList();
    }

    @Override
    public List<Sale> findIdOpportunity(Long idOportunidad) {
        return ventaRepository.findByIdOportunidad(idOportunidad).stream().map(ventaMapper::toDomain).toList();
    }

    @Override
    public Sale saveSale(Sale sale) {
        return ventaMapper.toDomain(ventaRepository.save(ventaMapper.toEntity(sale)));
    }

}
