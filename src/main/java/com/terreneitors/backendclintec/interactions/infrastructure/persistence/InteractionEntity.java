package com.terreneitors.backendclintec.interactions.infrastructure.persistence;

import com.terreneitors.backendclintec.interactions.domain.TypeInteraction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "interacciones")
@Getter
@Setter
public class InteractionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "oportunidad_id", nullable = false)
    private Long oportunidadId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeInteraction tipo;

    @Column(columnDefinition = "TEXT")
    private String nota;

    @Column(name = "fecha", updatable = false)
    private LocalDateTime fecha;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    void prePersist() {
        this.fecha              = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}
