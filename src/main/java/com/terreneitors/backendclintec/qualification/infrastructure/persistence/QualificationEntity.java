package com.terreneitors.backendclintec.qualification.infrastructure.persistence;

import com.terreneitors.backendclintec.qualification.domain.Qualification;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "calificacion_cliente")
@Getter
@Setter
public class QualificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cliente_id", nullable = false, unique = true)
    private Long clienteId;

    @Column(nullable = false)
    private int puntaje;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Qualification clasificacion;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    @PrePersist
    void prePersist() {
        this.ultimaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        this.ultimaActualizacion = LocalDateTime.now();
    }
}
