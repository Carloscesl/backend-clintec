package com.terreneitors.backendclintec.alerts.infrastructure.persistence;

import com.terreneitors.backendclintec.alerts.domain.StateAlert;
import com.terreneitors.backendclintec.alerts.domain.TypeAlert;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "alertas")
@Getter
@Setter
public class AlertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeAlert tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StateAlert estado;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDateTime fechaVencimiento;

    @Column(name = "fecha", updatable = false)
    private LocalDateTime fecha;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    void prePersist() {
        this.fecha              = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        this.estado             = StateAlert.PENDIENTE;
    }

    @PreUpdate
    void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}
