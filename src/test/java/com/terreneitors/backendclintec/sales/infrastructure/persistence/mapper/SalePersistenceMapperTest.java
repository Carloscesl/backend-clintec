package com.terreneitors.backendclintec.sales.infrastructure.persistence.mapper;

import com.terreneitors.backendclintec.sales.domain.PaymentMethod;
import com.terreneitors.backendclintec.sales.domain.Sale;
import com.terreneitors.backendclintec.sales.infrastructure.dto.SaleResponseDTO;
import com.terreneitors.backendclintec.sales.infrastructure.persistence.SaleEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SalePersistenceMapperTest {

    private final SalePersistenceMapper mapper = new SalePersistenceMapper();

    @Test
    @DisplayName("Debe mapear correctamente de Entity a Domain")
    void toDomain_ShouldMapCorrectly() {
        SaleEntity entity = new SaleEntity();
        entity.setIdVenta(1L);
        entity.setValorVenta(new BigDecimal("1500.00"));
        entity.setMetodoPago(PaymentMethod.TARJETA_CREDITO); // Usamos el Enum

        Sale domain = mapper.toDomain(entity);

        assertEquals(1L, domain.getIdVenta());
        assertEquals(new BigDecimal("1500.00"), domain.getValorVenta());
        assertEquals(PaymentMethod.TARJETA_CREDITO, domain.getMetodoPago());
    }

    @Test
    @DisplayName("Debe mapear correctamente de Domain a Entity")
    void toEntity_ShouldMapCorrectly() {
        Sale domain = new Sale();
        domain.setIdVenta(2L);
        domain.setNotas("Venta cerrada exitosamente");
        domain.setValorVenta(new BigDecimal("2000.00"));

        SaleEntity entity = mapper.toEntity(domain);

        assertEquals(2L, entity.getIdVenta());
        assertEquals("Venta cerrada exitosamente", entity.getNotas());
        assertEquals(new BigDecimal("2000.00"), entity.getValorVenta());
    }

    @Test
    @DisplayName("Debe mapear correctamente a DTO")
    void toDTO_ShouldMapCorrectly() {
        Sale domain = new Sale();
        domain.setIdVenta(3L);
        domain.setFechaVenta(LocalDateTime.now());
        domain.setMetodoPago(PaymentMethod.EFECTIVO);

        SaleResponseDTO dto = mapper.toDTO(domain);

        assertEquals(3L, dto.idVenta());
        assertEquals(PaymentMethod.EFECTIVO, dto.paymentMethod());
        assertNotNull(dto.fechaVenta());
    }
}