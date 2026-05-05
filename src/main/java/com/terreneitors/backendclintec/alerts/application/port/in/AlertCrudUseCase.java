package com.terreneitors.backendclintec.alerts.application.port.in;

import com.terreneitors.backendclintec.alerts.domain.Alert;
import com.terreneitors.backendclintec.alerts.domain.StateAlert;

import java.util.List;
import java.util.Optional;

public interface AlertCrudUseCase {
    List<Alert> findAll();
    Optional<Alert> buscarPorId(Long id);
    List<Alert>     buscarPorCliente(Long clienteId);
    List<Alert>     buscarPorUsuario(Long usuarioId);
    List<Alert>     buscarPendientes();

    long countByEstado(StateAlert estado);
    long countByUsuarioIdAndEstado(Long usuarioId, StateAlert estado);
    Alert marcarComoVista(Long id);
    Alert resolver(Long id);
}
