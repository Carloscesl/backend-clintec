package com.terreneitors.backendclintec.alerts.application.service;

import com.terreneitors.backendclintec.alerts.application.port.in.AlertCrudUseCase;
import com.terreneitors.backendclintec.alerts.application.port.out.AlertRepositoryPort;
import com.terreneitors.backendclintec.alerts.domain.Alert;
import com.terreneitors.backendclintec.alerts.domain.StateAlert;
import com.terreneitors.backendclintec.shared.exception.InvalidStateException;
import com.terreneitors.backendclintec.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertCrudService implements AlertCrudUseCase {
    private final AlertRepositoryPort alertRepositoryPort;

    @Override
    public List<Alert> findAll() {
        return alertRepositoryPort.findAll();
    }

    @Override
    public Optional<Alert> buscarPorId(Long id) {
        return alertRepositoryPort.findById(id);
    }

    @Override
    public List<Alert> buscarPorCliente(Long clienteId) {
        return alertRepositoryPort.findByClienteId(clienteId);
    }

    @Override
    public List<Alert> buscarPorUsuario(Long usuarioId) {
        return alertRepositoryPort.findByUsuarioId(usuarioId);
    }

    @Override
    public List<Alert> buscarPendientes() {
        return alertRepositoryPort.findPendientes();
    }

    @Override
    public Alert marcarComoVista(Long id) {
        log.info("[ALERTA_VISTA] id={}", id);

        Alert alerta = alertRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert", "id", id));

        if (alerta.getEstado() == StateAlert.RESUELTA) {
            throw new InvalidStateException(
                    "No se puede marcar como vista una alerta ya resuelta.");
        }

        alerta.marcarComoVista();
        Alert actualizada = alertRepositoryPort.save(alerta);
        log.info("[ALERTA_MARCADA_VISTA] id={}", id);

        return actualizada;
    }

    @Override
    public Alert resolver(Long id) {
        log.info("[ALERTA_RESOLVER] id={}", id);

        Alert alerta = alertRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert", "id", id));

        if (alerta.getEstado() == StateAlert.RESUELTA) {
            throw new InvalidStateException(
                    "La alerta con id " + id + " ya está resuelta.");
        }

        alerta.resolver();
        Alert actualizada = alertRepositoryPort.save(alerta);
        log.info("[ALERTA_RESUELTA] id={}", id);

        return actualizada;
    }
}
