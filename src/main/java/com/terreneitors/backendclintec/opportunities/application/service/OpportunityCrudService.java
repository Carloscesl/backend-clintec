package com.terreneitors.backendclintec.opportunities.application.service;

import com.terreneitors.backendclintec.clients.application.port.out.ClientRepositoryPort;
import com.terreneitors.backendclintec.opportunities.application.port.in.OpportunityCrudUseCase;
import com.terreneitors.backendclintec.opportunities.application.port.out.OpportunityRepositoryPort;
import com.terreneitors.backendclintec.opportunities.domain.StatusOpportunity;
import com.terreneitors.backendclintec.opportunities.domain.StageOpportunity;
import com.terreneitors.backendclintec.opportunities.domain.Opportunity;
import com.terreneitors.backendclintec.opportunities.infrastructure.dto.OpportunityRequestDTO;
import com.terreneitors.backendclintec.shared.exception.BusinessException;
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
public class OpportunityCrudService implements OpportunityCrudUseCase {

    private final OpportunityRepositoryPort oportunidadesRespositoryPort;
    private final ClientRepositoryPort clientRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;


    @Override
    public List<Opportunity> findAll() {
        return oportunidadesRespositoryPort.findAll();
    }

    @Override
    public Optional<Opportunity> findById(Long id) {
        return oportunidadesRespositoryPort.findById(id);
    }

    @Override
    public List<Opportunity> findByIdAssessor(Long id) {
        return oportunidadesRespositoryPort.findByAssessor(id);
    }

    @Override
    public List<Opportunity> findByIdClient(Long id) {
        return oportunidadesRespositoryPort.findByIdClient(id);
    }

    @Override
    public Opportunity createOpportunities(OpportunityRequestDTO dto) {

        log.info("[OPORTUNIDAD_CREAR] clienteId={} | asesorId={} | valorEstimado={}", dto.clienteId(), dto.asesorId(), dto.valorEstimado());

        clientRepositoryPort.findById(dto.clienteId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente", "id", dto.clienteId()));

        userRepositoryPort.findById(dto.asesorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Asesor", "id", dto.asesorId()));

        Opportunity nueva = new Opportunity();
        nueva.setClienteId(dto.clienteId());
        nueva.setAsesorId(dto.asesorId());
        nueva.setDescripcion(dto.descripcion());
        nueva.setValorEstimado(dto.valorEstimado());
        nueva.setFechaEstimadaCierre(dto.fechaCierreEstimada());
        nueva.setEstado(StatusOpportunity.ACTIVA);
        nueva.setEtapaOportunidad(StageOpportunity.PROSPECCIÓN);
        nueva.setProbabilidad(StageOpportunity.PROSPECCIÓN.getProbabilidadDefault());

        Opportunity guardada = oportunidadesRespositoryPort.save(nueva);

        if (guardada == null || guardada.getIdOportunidad() == null) {
            log.error("[OPORTUNIDAD_CREAR_FALLIDO] clienteId={} | asesorId={}",
                    dto.clienteId(), dto.asesorId());
            throw new BusinessException("ERROR_CREAR_OPORTUNIDAD",
                    "No se pudo crear la oportunidad. Intenta de nuevo.");
        }

        log.info("[OPORTUNIDAD_CREADA] id={} | etapa={} | probabilidad={}%",
                guardada.getIdOportunidad(), guardada.getEtapaOportunidad(),
                guardada.getProbabilidad());

        return guardada;

    }

    @Override
    public Opportunity updateOpportunities(Long id, OpportunityRequestDTO dto) {

        log.info("[OPORTUNIDAD_ACTUALIZAR] id={}", id);

        Opportunity opportunity = oportunidadesRespositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Oportunidad", "id", id));

        if (opportunity.getEstado() == StatusOpportunity.GANADA ||
                opportunity.getEstado() == StatusOpportunity.PERDIDA) {
            throw new InvalidStateException(
                    "No se puede modificar una oportunidad cerrada. Estado actual: "
                            + opportunity.getEstado());
        }

        opportunity.setDescripcion(dto.descripcion());
        opportunity.setValorEstimado(dto.valorEstimado());
        opportunity.setFechaEstimadaCierre(dto.fechaCierreEstimada());

        Opportunity actualizada = oportunidadesRespositoryPort.save(opportunity);
        log.info("[OPORTUNIDAD_ACTUALIZADA] id={} | valor={}",
                id, actualizada.getValorEstimado());

        return actualizada;
    }

    @Override
    public Opportunity changeStage(Long id, StageOpportunity nuevaEtapa) {

        log.info("[OPORTUNIDAD_CAMBIAR_ETAPA] id={} | nuevaEtapa={}", id, nuevaEtapa);

        Opportunity opportunity = oportunidadesRespositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Oportunidad", "id", id));

        if (opportunity.getEstado() == StatusOpportunity.GANADA ||
                opportunity.getEstado() == StatusOpportunity.PERDIDA) {
            throw new InvalidStateException(
                    "No se puede cambiar la etapa de una oportunidad cerrada. Estado: "
                            + opportunity.getEstado());
        }

        Opportunity actualizada = oportunidadesRespositoryPort.save(opportunity);

        log.info("[OPORTUNIDAD_ETAPA_CAMBIADA] id={} | etapa={} | probabilidad={}%",
                id, actualizada.getEtapaOportunidad(), actualizada.getProbabilidad());

        return actualizada;
    }

    @Override
    public Opportunity adjustProbability(Long id, int probabilidad) {

        log.info("[OPORTUNIDAD_AJUSTAR_PROBABILIDAD] id={} | probabilidad={}%",
                id, probabilidad);

        Opportunity opportunity = oportunidadesRespositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Oportunidad", "id", id));

        int min = opportunity.getEtapaOportunidad().getMin();
        int max = opportunity.getEtapaOportunidad().getMax();

        if (probabilidad < min || probabilidad > max) {
            throw new ValidationException(
                    "Para la etapa " + opportunity.getEtapaOportunidad() +
                            " la probabilidad debe estar entre " + min + "% y " + max + "%." +
                            " Valor recibido: " + probabilidad + "%");
        }

        opportunity.setProbabilidad(probabilidad);

        Opportunity actualizada = oportunidadesRespositoryPort.save(opportunity);
        log.info("[OPORTUNIDAD_PROBABILIDAD_AJUSTADA] id={} | probabilidad={}%",
                id, actualizada.getProbabilidad());

        return actualizada;
    }

    @Override
    public void closeAsWon(Long id) {

        log.info("[OPORTUNIDAD_CERRAR_GANADA] id={}", id);

        Opportunity opportunity = oportunidadesRespositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Oportunidad", "id", id));

        if (opportunity.getEstado() == StatusOpportunity.GANADA) {
            throw new InvalidStateException(
                    "La oportunidad con id " + id + " ya está cerrada como GANADA.");
        }
        if (opportunity.getEstado() == StatusOpportunity.PERDIDA) {
            throw new InvalidStateException(
                    "No se puede ganar una oportunidad que ya fue cerrada como PERDIDA.");
        }

        opportunity.cerrarComoGanada();
        oportunidadesRespositoryPort.save(opportunity);

        log.info("[OPORTUNIDAD_GANADA] id={} | asesorId={} | valor={}",
                id, opportunity.getAsesorId(), opportunity.getValorEstimado());
    }

    @Override
    public void closeAsLost(Long id) {

        log.info("[OPORTUNIDAD_CERRAR_PERDIDA] id={}", id);

        Opportunity opportunity = oportunidadesRespositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Oportunidad", "id", id));

        if (opportunity.getEstado() == StatusOpportunity.PERDIDA) {
            throw new InvalidStateException(
                    "La oportunidad con id " + id + " ya está cerrada como PERDIDA.");
        }
        if (opportunity.getEstado() == StatusOpportunity.GANADA) {
            throw new InvalidStateException(
                    "No se puede perder una oportunidad que ya fue cerrada como GANADA.");
        }

        opportunity.cerrarComoPerdida();
        oportunidadesRespositoryPort.save(opportunity);

        log.info("[OPORTUNIDAD_PERDIDA] id={} | asesorId={} | valor={}",
                id, opportunity.getAsesorId(), opportunity.getValorEstimado());
    }
}
