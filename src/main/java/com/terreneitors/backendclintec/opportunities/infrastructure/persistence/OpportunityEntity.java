package com.terreneitors.backendclintec.opportunities.infrastructure.persistence;

import com.terreneitors.backendclintec.opportunities.domain.StatusOpportunity;
import com.terreneitors.backendclintec.opportunities.domain.StageOpportunity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "oportunidades")
@Getter
@Setter
public class OpportunityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oportunidad")
    private Long id;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(name = "asesor_id", nullable = false)
    private Long asesorId;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "valor_estimado", precision = 12, scale = 2)
    private BigDecimal valorEstimado;

    @Column(nullable = false)
    private int probabilidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StageOpportunity etapa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOpportunity estado;

    @Column(name = "fecha_cierre_estimada")
    private LocalDate fechaCierreEstimada;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    void prePersist() {
        this.fechaCreacion      = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

}