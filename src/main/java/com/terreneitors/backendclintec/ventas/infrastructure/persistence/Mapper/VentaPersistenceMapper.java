package com.terreneitors.backendclintec.ventas.infrastructure.persistence.Mapper;

import com.terreneitors.backendclintec.ventas.domain.Venta;
import com.terreneitors.backendclintec.ventas.infrastructure.dto.VentaResponseDTO;
import com.terreneitors.backendclintec.ventas.infrastructure.persistence.VentaEntity;
import org.springframework.stereotype.Component;

@Component
public class VentaPersistenceMapper {
    public Venta toDomain(VentaEntity entity) {
        Venta v = new Venta();
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

    public VentaEntity toEntity(Venta v) {
        VentaEntity entity = new VentaEntity();
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

    public VentaResponseDTO toDTO(Venta v) {
        return new VentaResponseDTO(
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
