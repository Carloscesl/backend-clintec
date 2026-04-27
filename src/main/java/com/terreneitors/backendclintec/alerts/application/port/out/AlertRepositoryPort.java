package com.terreneitors.backendclintec.alerts.application.port.out;

import com.terreneitors.backendclintec.alerts.domain.Alert;
import com.terreneitors.backendclintec.alerts.domain.TypeAlert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AlertRepositoryPort {
    Alert save(Alert alerta);
    Optional<Alert> findById(Long id);
    List<Alert> findAll();
    List<Alert>     findByClienteId(Long clienteId);
    List<Alert>     findByUsuarioId(Long usuarioId);
    List<Alert>     findPendientes();

    // Para el Scheduler — verificar si ya existe alerta del mismo tipo
    boolean existeAlertPendiente(Long clienteId, TypeAlert tipo);

    // Queries para el Scheduler
    List<Long> findClientesSinInteraccionesDesdeFecha(LocalDateTime fecha);
    List<Long> findOportunidadesVencidasActivas(LocalDateTime fecha);
    List<Long> findOportunidadesEnNegociacionSinCambios(LocalDateTime fecha);
    List<Long> findClientesSinOportunidadesDesdeFecha(LocalDateTime fecha);
}
