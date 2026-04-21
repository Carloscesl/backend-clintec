package com.terreneitors.backendclintec.sales.infrastructure.persistence;

import com.terreneitors.backendclintec.sales.domain.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ventas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idVenta")
    private Long idVenta;
    @Column(name = "idOportunidad",nullable = false)
    private Long idOportunidad;
    @Column(name = "valorVenta", nullable = false,precision = 12, scale = 2)
    private BigDecimal valorVenta;
    @Column(name = "idAsesor", nullable = false)
    private Long idAsesor;
    @Column(name = "fechaVenta")
    private LocalDateTime fechaVenta;
    @Column(name = "fechaActualizacion")
    private LocalDateTime fechaActualizacion;
    @Column(name = "notas", columnDefinition = "Text")
    private String notas;
    @Enumerated(EnumType.STRING)
    @Column(name = "metodoPago")
    private PaymentMethod paymentMethod;

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
