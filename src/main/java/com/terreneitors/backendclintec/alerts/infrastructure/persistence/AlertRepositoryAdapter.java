package com.terreneitors.backendclintec.alerts.infrastructure.persistence;

import com.terreneitors.backendclintec.alerts.application.port.out.AlertRepositoryPort;
import com.terreneitors.backendclintec.alerts.domain.Alert;
import com.terreneitors.backendclintec.alerts.domain.StateAlert;
import com.terreneitors.backendclintec.alerts.domain.TypeAlert;
import com.terreneitors.backendclintec.alerts.infrastructure.persistence.Mapper.AlertPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AlertRepositoryAdapter implements AlertRepositoryPort {

    private final SpringAlertRepository springAlertRepository;
    private final AlertPersistenceMapper mapper;


    @Override
    public Alert save(Alert alerta) {
        return mapper.toDomain(springAlertRepository.save(mapper.toEntity(alerta)));
    }

    @Override
    public Optional<Alert> findById(Long id) {
        return springAlertRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Alert> findAll() {
        return springAlertRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Alert> findByClienteId(Long clienteId) {
        return springAlertRepository.findByClienteId(clienteId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Alert> findByUsuarioId(Long usuarioId) {
        return springAlertRepository.findByUsuarioId(usuarioId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Alert> findPendientes() {
        return springAlertRepository.findByEstado(StateAlert.PENDIENTE)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existeAlertPendiente(Long clienteId, TypeAlert tipo) {
        return springAlertRepository.existsByClienteIdAndTipoAndEstado(
                clienteId, tipo, StateAlert.PENDIENTE);
    }

    @Override
    public List<Long> findClientesSinInteraccionesDesdeFecha(LocalDateTime fecha) {
        return springAlertRepository.findClientesSinInteraccionesDesdeFecha(fecha);
    }

    @Override
    public List<Long> findOportunidadesVencidasActivas(LocalDateTime fecha) {
        return springAlertRepository.findOportunidadesVencidasActivas(fecha);
    }

    @Override
    public List<Long> findOportunidadesEnNegociacionSinCambios(LocalDateTime fecha) {
        return springAlertRepository.findOportunidadesEnNegociacionSinCambios(fecha);
    }

    @Override
    public List<Long> findClientesSinOportunidadesDesdeFecha(LocalDateTime fecha) {
        return springAlertRepository.findClientesSinOportunidadesDesdeFecha(fecha);
    }
}
