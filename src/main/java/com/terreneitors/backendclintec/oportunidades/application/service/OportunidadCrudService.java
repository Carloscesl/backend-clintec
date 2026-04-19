package com.terreneitors.backendclintec.oportunidades.application.service;

import com.terreneitors.backendclintec.clientes.application.port.out.ClienteRepositoryPort;
import com.terreneitors.backendclintec.oportunidades.application.port.in.OportunidadCrudUseCase;
import com.terreneitors.backendclintec.oportunidades.application.port.out.OportunidadRespositoryPort;
import com.terreneitors.backendclintec.oportunidades.domain.EstadoOportunidad;
import com.terreneitors.backendclintec.oportunidades.domain.EtapaOportunidad;
import com.terreneitors.backendclintec.oportunidades.domain.Oportunidad;
import com.terreneitors.backendclintec.oportunidades.infrastructure.dto.OportunidadRequestDTO;
import com.terreneitors.backendclintec.shared.exception.BusinessException;
import com.terreneitors.backendclintec.shared.exception.InvalidStateException;
import com.terreneitors.backendclintec.shared.exception.ResourceNotFoundException;
import com.terreneitors.backendclintec.shared.exception.ValidationException;
import com.terreneitors.backendclintec.usuarios.application.port.out.UsuarioRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OportunidadCrudService implements OportunidadCrudUseCase {

    private final OportunidadRespositoryPort oportunidadesRespositoryPort;
    private final ClienteRepositoryPort clienteRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;


    @Override
    public List<Oportunidad> findAll() {
        return oportunidadesRespositoryPort.findAll();
    }

    @Override
    public Optional<Oportunidad> buscarPorId(Long id) {
        return oportunidadesRespositoryPort.findById(id);
    }

    @Override
    public List<Oportunidad> buscarPorIdAsesor(Long id) {
        return oportunidadesRespositoryPort.findByAsesor(id);
    }

    @Override
    public List<Oportunidad> buscarPorIdCliente(Long id) {
        return oportunidadesRespositoryPort.findByIdCliente(id);
    }

    @Override
    public Oportunidad crearOportunidades(OportunidadRequestDTO dto) {

        log.info("[OPORTUNIDAD_CREAR] clienteId={} | asesorId={} | valorEstimado={}", dto.clienteId(), dto.asesorId(), dto.valorEstimado());

        clienteRepositoryPort.findById(dto.clienteId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente", "id", dto.clienteId()));

        usuarioRepositoryPort.findById(dto.asesorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Asesor", "id", dto.asesorId()));

        Oportunidad nueva = new Oportunidad();
        nueva.setClienteId(dto.clienteId());
        nueva.setAsesorId(dto.asesorId());
        nueva.setDescripcion(dto.descripcion());
        nueva.setValorEstimado(dto.valorEstimado());
        nueva.setFechaEstimadaCierre(dto.fechaCierreEstimada());
        nueva.setEstado(EstadoOportunidad.ACTIVA);
        nueva.setEtapaOportunidad(EtapaOportunidad.PROSPECCIÓN);
        nueva.setProbabilidad(EtapaOportunidad.PROSPECCIÓN.getProbabilidadDefault());

        Oportunidad guardada = oportunidadesRespositoryPort.save(nueva);

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
    public Oportunidad actulizarOportunidades(Long id, OportunidadRequestDTO dto) {

        log.info("[OPORTUNIDAD_ACTUALIZAR] id={}", id);

        Oportunidad oportunidad = oportunidadesRespositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Oportunidad", "id", id));

        if (oportunidad.getEstado() == EstadoOportunidad.GANADA ||
                oportunidad.getEstado() == EstadoOportunidad.PERDIDA) {
            throw new InvalidStateException(
                    "No se puede modificar una oportunidad cerrada. Estado actual: "
                            + oportunidad.getEstado());
        }

        oportunidad.setDescripcion(dto.descripcion());
        oportunidad.setValorEstimado(dto.valorEstimado());
        oportunidad.setFechaEstimadaCierre(dto.fechaCierreEstimada());

        Oportunidad actualizada = oportunidadesRespositoryPort.save(oportunidad);
        log.info("[OPORTUNIDAD_ACTUALIZADA] id={} | valor={}",
                id, actualizada.getValorEstimado());

        return actualizada;
    }

    @Override
    public Oportunidad cambiarEtapa(Long id, EtapaOportunidad nuevaEtapa) {

        log.info("[OPORTUNIDAD_CAMBIAR_ETAPA] id={} | nuevaEtapa={}", id, nuevaEtapa);

        Oportunidad oportunidad = oportunidadesRespositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Oportunidad", "id", id));

        if (oportunidad.getEstado() == EstadoOportunidad.GANADA ||
                oportunidad.getEstado() == EstadoOportunidad.PERDIDA) {
            throw new InvalidStateException(
                    "No se puede cambiar la etapa de una oportunidad cerrada. Estado: "
                            + oportunidad.getEstado());
        }

        Oportunidad actualizada = oportunidadesRespositoryPort.save(oportunidad);

        log.info("[OPORTUNIDAD_ETAPA_CAMBIADA] id={} | etapa={} | probabilidad={}%",
                id, actualizada.getEtapaOportunidad(), actualizada.getProbabilidad());

        return actualizada;
    }

    @Override
    public Oportunidad ajustarProbabilidad(Long id, int probabilidad) {

        log.info("[OPORTUNIDAD_AJUSTAR_PROBABILIDAD] id={} | probabilidad={}%",
                id, probabilidad);

        Oportunidad oportunidad = oportunidadesRespositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Oportunidad", "id", id));

        int min = oportunidad.getEtapaOportunidad().getMin();
        int max = oportunidad.getEtapaOportunidad().getMax();

        if (probabilidad < min || probabilidad > max) {
            throw new ValidationException(
                    "Para la etapa " + oportunidad.getEtapaOportunidad() +
                            " la probabilidad debe estar entre " + min + "% y " + max + "%." +
                            " Valor recibido: " + probabilidad + "%");
        }

        oportunidad.setProbabilidad(probabilidad);

        Oportunidad actualizada = oportunidadesRespositoryPort.save(oportunidad);
        log.info("[OPORTUNIDAD_PROBABILIDAD_AJUSTADA] id={} | probabilidad={}%",
                id, actualizada.getProbabilidad());

        return actualizada;
    }

    @Override
    public void cerrarComoGanada(Long id) {

        log.info("[OPORTUNIDAD_CERRAR_GANADA] id={}", id);

        Oportunidad oportunidad = oportunidadesRespositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Oportunidad", "id", id));

        if (oportunidad.getEstado() == EstadoOportunidad.GANADA) {
            throw new InvalidStateException(
                    "La oportunidad con id " + id + " ya está cerrada como GANADA.");
        }
        if (oportunidad.getEstado() == EstadoOportunidad.PERDIDA) {
            throw new InvalidStateException(
                    "No se puede ganar una oportunidad que ya fue cerrada como PERDIDA.");
        }

        oportunidad.cerrarComoGanada();
        oportunidadesRespositoryPort.save(oportunidad);

        log.info("[OPORTUNIDAD_GANADA] id={} | asesorId={} | valor={}",
                id, oportunidad.getAsesorId(), oportunidad.getValorEstimado());
    }

    @Override
    public void cerrarComoPerdida(Long id) {

        log.info("[OPORTUNIDAD_CERRAR_PERDIDA] id={}", id);

        Oportunidad oportunidad = oportunidadesRespositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Oportunidad", "id", id));

        if (oportunidad.getEstado() == EstadoOportunidad.PERDIDA) {
            throw new InvalidStateException(
                    "La oportunidad con id " + id + " ya está cerrada como PERDIDA.");
        }
        if (oportunidad.getEstado() == EstadoOportunidad.GANADA) {
            throw new InvalidStateException(
                    "No se puede perder una oportunidad que ya fue cerrada como GANADA.");
        }

        oportunidad.cerrarComoPerdida();
        oportunidadesRespositoryPort.save(oportunidad);

        log.info("[OPORTUNIDAD_PERDIDA] id={} | asesorId={} | valor={}",
                id, oportunidad.getAsesorId(), oportunidad.getValorEstimado());
    }
}
