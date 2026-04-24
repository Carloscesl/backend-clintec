package com.terreneitors.backendclintec.interactions.application.service;

import com.terreneitors.backendclintec.clients.application.port.out.ClientRepositoryPort;
import com.terreneitors.backendclintec.interactions.application.port.in.InteractionCrudUseCase;
import com.terreneitors.backendclintec.interactions.application.port.out.InteractionRepositoryPort;
import com.terreneitors.backendclintec.interactions.domain.Interaction;
import com.terreneitors.backendclintec.interactions.infrastructure.dto.InteractionRequestDTO;
import com.terreneitors.backendclintec.opportunities.application.port.out.OpportunityRepositoryPort;
import com.terreneitors.backendclintec.opportunities.domain.Opportunity;
import com.terreneitors.backendclintec.opportunities.domain.StatusOpportunity;
import com.terreneitors.backendclintec.shared.exception.InvalidStateException;
import com.terreneitors.backendclintec.shared.exception.ResourceNotFoundException;
import com.terreneitors.backendclintec.shared.exception.ValidationException;
import com.terreneitors.backendclintec.users.application.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InteractionCrudService implements InteractionCrudUseCase {
    private final InteractionRepositoryPort interactionRepositoryPort;
    private final ClientRepositoryPort clientRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final OpportunityRepositoryPort opportunityRepositoryPort;

    @Override
    public List<Interaction> findAll() {
        return interactionRepositoryPort.findAll();
    }

    @Override
    public Optional<Interaction> findById(Long id) {
        return interactionRepositoryPort.findById(id);
    }

    @Override
    public List<Interaction> findByIdClient(Long clienteId) {
        return interactionRepositoryPort.findByClienteId(clienteId);
    }

    @Override
    public List<Interaction> findByIdOpportunities(Long oportunidadId) {
        return interactionRepositoryPort.findByOportunidadId(oportunidadId);
    }

    @Override
    public List<Interaction> findByIdUser(Long usuarioId) {
        return interactionRepositoryPort.findByUsuarioId(usuarioId);
    }

    @Override
    public Interaction createInteraction(InteractionRequestDTO dto) {
        log.info("[INTERACCION_CREAR] clienteId={} | oportunidadId={} | tipo={}",
                dto.clienteId(), dto.oportunidadId(), dto.tipo());
        clientRepositoryPort.findById(dto.clienteId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente", "id", dto.clienteId()));

        userRepositoryPort.findById(dto.clienteId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario", "id", dto.usuarioId()));

        Opportunity oportunidad = opportunityRepositoryPort
                .findById(dto.clienteId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Oportunidad", "id", dto.oportunidadId()));

        if (oportunidad.getEstado() == StatusOpportunity.PERDIDA){
            throw new InvalidStateException(
                    "No se puede registrar una interacción sobre una oportunidad PERDIDA.");
        }

        if (!oportunidad.getClienteId().equals(dto.equals(dto.clienteId()))){
            throw new ValidationException(
                    "El cliente de la oportunidad no coincide con el cliente indicado."
            );
        }

        Interaction nueva = new Interaction();
        nueva.setClienteId(dto.clienteId());
        nueva.setUsuarioId(dto.usuarioId());
        nueva.setOportunidadId(dto.oportunidadId());
        nueva.setTipo(dto.tipo());
        nueva.setNota(dto.nota());

        Interaction guardada = interactionRepositoryPort.save(nueva);

        if (guardada == null || guardada.getId() == null) {
            log.error("[INTERACCION_CREAR_FALLIDO] clienteId={} | oportunidadId={}",
                    dto.clienteId(), dto.oportunidadId());
            throw new ValidationException(
                    "No se pudo registrar la interacción. Intenta de nuevo.");
        }

        log.info("[INTERACCION_CREADA] id={} | clienteId={} | tipo={}",
                guardada.getId(), guardada.getClienteId(), guardada.getTipo());

        return guardada;
    }

    @Override
    public Interaction updateInteraction(Long id, InteractionRequestDTO dto) {
        log.info("[INTERACCION_ACTUALIZAR] id={}", id);

        Interaction interaccion = interactionRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interaccion", "id", id));

        interaccion.setTipo(dto.tipo());
        interaccion.setNota(dto.nota());

        Interaction actualizada = interactionRepositoryPort.save(interaccion);
        log.info("[INTERACCION_ACTUALIZADA] id={} | tipo={}", id, actualizada.getTipo());

        return actualizada;
    }
}
