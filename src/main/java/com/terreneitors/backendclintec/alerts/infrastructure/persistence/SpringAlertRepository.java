package com.terreneitors.backendclintec.alerts.infrastructure.persistence;

import com.terreneitors.backendclintec.alerts.domain.StateAlert;
import com.terreneitors.backendclintec.alerts.domain.TypeAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SpringAlertRepository extends JpaRepository<AlertEntity, Long> {
    List<AlertEntity> findByClienteId(Long clienteId);
    List<AlertEntity> findByUsuarioId(Long usuarioId);
    List<AlertEntity> findByEstado(StateAlert estado);

    boolean existsByClienteIdAndTipoAndEstado(
            Long clienteId,
            TypeAlert tipo,
            StateAlert estado
    );

    // Clientes sin interacciones desde una fecha
    @Query("""
        SELECT DISTINCT c.id FROM ClientEntity c
        WHERE c.id NOT IN (
            SELECT i.clienteId FROM InteractionEntity i
            WHERE i.fecha >= :fecha
        )
        """)
    List<Long> findClientesSinInteraccionesDesdeFecha(@Param("fecha") LocalDateTime fecha);

    // Oportunidades activas con fecha de cierre vencida
    @Query("""
        SELECT DISTINCT o.clienteId FROM OpportunityEntity o
        WHERE o.estado = 'ACTIVA'
        AND o.fechaCierreEstimada < :ahora
        """)
    List<Long> findOportunidadesVencidasActivas(@Param("ahora") LocalDateTime ahora);

    // Oportunidades en NEGOCIACIÓN sin cambios
    @Query("""
        SELECT DISTINCT o.clienteId FROM OpportunityEntity o
        WHERE o.etapa = 'NEGOCIACIÓN'
        AND o.fechaActualizacion < :fecha
        AND o.estado = 'ACTIVA'
        """)
    List<Long> findOportunidadesEnNegociacionSinCambios(@Param("fecha") LocalDateTime fecha);

    // Clientes sin oportunidades desde una fecha
    @Query("""
        SELECT DISTINCT c.id FROM ClientEntity c
        WHERE c.id NOT IN (
            SELECT o.clienteId FROM OpportunityEntity o
            WHERE o.fechaCreacion >= :fecha
            AND o.estado = 'ACTIVA'
        )
        """)
    List<Long> findClientesSinOportunidadesDesdeFecha(@Param("fecha") LocalDateTime fecha);
}
