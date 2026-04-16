package com.terreneitors.backendclintec.ventas.infrastructure.persistence;

import com.terreneitors.backendclintec.ventas.domain.MetodoPago;
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
public class ventaEntity {

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
    private MetodoPago metodoPago;
}
