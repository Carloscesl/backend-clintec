// qualification/infrastructure/persistence/QualificationHistoryEntity.java
package com.terreneitors.backendclintec.qualification.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "calificacion_historial")
public class QualificationHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(name = "puntaje_anterior", nullable = false)
    private int puntajeAnterior;

    @Column(name = "puntaje_nuevo", nullable = false)
    private int puntajeNuevo;

    @Column(nullable = false, length = 100)
    private String motivo;

    @Column(nullable = false)
    private LocalDateTime fecha;
}