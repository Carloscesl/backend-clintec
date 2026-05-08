package com.terreneitors.backendclintec.sales.infrastructure.persistence.Mapper;

import com.terreneitors.backendclintec.sales.domain.Sale;
import com.terreneitors.backendclintec.sales.infrastructure.dto.SaleResponseDTO;
import com.terreneitors.backendclintec.sales.infrastructure.persistence.SaleEntity;
import org.springframework.stereotype.Component;

@Component
public class SalePersistenceMapper {
    public Sale toDomain(SaleEntity entity) {
        Sale v = new Sale();
        v.setIdVenta(entity.getIdVenta());
        v.setIdOportunidad(entity.getIdOportunidad());
        v.setIdAsesor(entity.getIdAsesor());
        v.setValorVenta(entity.getValorVenta());
        v.setMetodoPago(entity.getMetodoPago());
        v.setNotas(entity.getNotas());
        v.setFechaVenta(entity.getFechaVenta());
        v.setFechaActualizacion(entity.getFechaActualizacion());
        return v;
    }

    public SaleEntity toEntity(Sale v) {
        SaleEntity entity = new SaleEntity();
        entity.setIdVenta(v.getIdVenta());
        entity.setIdOportunidad(v.getIdOportunidad());
        entity.setIdAsesor(v.getIdAsesor());
        entity.setValorVenta(v.getValorVenta());
        entity.setMetodoPago(v.getMetodoPago());
        entity.setNotas(v.getNotas());
        entity.setFechaVenta(v.getFechaVenta());
        entity.setFechaActualizacion(v.getFechaActualizacion());
        return entity;
    }

    public SaleResponseDTO toDTO(Sale v) {
        return new SaleResponseDTO(
                v.getIdVenta(),
                v.getIdOportunidad(),
                v.getIdAsesor(),
                v.getValorVenta(),
                v.getMetodoPago(),
                v.getNotas(),
                v.getFechaVenta(),
                v.getFechaActualizacion());
    }
}
