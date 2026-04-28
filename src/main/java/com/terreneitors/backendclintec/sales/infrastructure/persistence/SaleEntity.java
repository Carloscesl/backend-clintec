package com.terreneitors.backendclintec.sales.infrastructure.persistence;

import com.terreneitors.backendclintec.sales.domain.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ventas")
@Getter
@Setter
public class SaleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Long idVenta;

    // ✅ FK hacia oportunidades
    @Column(name = "id_oportunidad", nullable = false)
    private Long idOportunidad;

    // ✅ FK hacia usuarios
    @Column(name = "id_asesor", nullable = false)
    private Long idAsesor;

    @Column(name = "valor_venta", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorVenta;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago")
    private  PaymentMethod metodoPago;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @Column(name = "fecha_venta", updatable = false)
    private LocalDateTime fechaVenta;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    void prePersist() {
        this.fechaVenta         = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}
