package com.terreneitors.backendclintec.oportunidades.infrastructure.persistence;

import com.terreneitors.backendclintec.oportunidades.domain.EstadoOportunidad;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "oportunidades")
@Getter
@Setter
public class OportunidadesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOportunidades")
    private Long id;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(name = "asesor_id", nullable = false)
    private Long asesorId;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "valor_estimado", precision = 12, scale = 2)
    private BigDecimal valorEstimado;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoOportunidad estado;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    public void prePersist() {
        if (this.fechaCreacion == null) {
            this.fechaCreacion = LocalDateTime.now();
        }
    }

}