package com.terreneitors.backendclintec.oportunidades.application.service;

import com.terreneitors.backendclintec.oportunidades.application.port.in.OportunidadesCrudUseCase;
import com.terreneitors.backendclintec.oportunidades.application.port.out.OportunidadesRespositoryPort;
import com.terreneitors.backendclintec.oportunidades.domain.EstadoOportunidad;
import com.terreneitors.backendclintec.oportunidades.domain.EtapaOportunidad;
import com.terreneitors.backendclintec.oportunidades.domain.Oportunidad;
import com.terreneitors.backendclintec.oportunidades.infrastructure.dto.OportunidadRequestDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OportunidadesCrudService implements OportunidadesCrudUseCase {
    private OportunidadesRespositoryPort oportunidadesRespositoryPort;

    public OportunidadesCrudService(OportunidadesRespositoryPort oportunidadesRespositoryPort) {
        this.oportunidadesRespositoryPort = oportunidadesRespositoryPort;
    }

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
    public Oportunidad crearOportunidades(OportunidadRequestDTO oportunidad) {
        Oportunidad nueva = new Oportunidad();
        nueva.setClienteId(oportunidad.clienteId());
        nueva.setAsesorId(oportunidad.asesorId());
        nueva.setDescripcion(oportunidad.descripcion());
        nueva.setValorEstimado(oportunidad.valorEstimado());
        nueva.setFechaEstimadaCierre(oportunidad.fechaCierreEstimada());
        nueva.setFechaCreacion(LocalDateTime.now());
        nueva.setFechaActualizacion(LocalDateTime.now());
        nueva.setEstado(EstadoOportunidad.ACTIVA);
        nueva.setEtapaOportunidad(EtapaOportunidad.PROSPECCIÓN);
        nueva.setProbabilidad(EtapaOportunidad.PROSPECCIÓN.getProbabilidadDefault());
        return oportunidadesRespositoryPort.save(nueva);
    }

    @Override
    public Oportunidad actulizarOportunidades(Long id, OportunidadRequestDTO oportunidad) {
        Oportunidad newoportunidad = oportunidadesRespositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Oportunidad no encontrada con id: " + id));
        newoportunidad.setDescripcion(oportunidad.descripcion());
        newoportunidad.setValorEstimado(oportunidad.valorEstimado());
        newoportunidad.setFechaEstimadaCierre(oportunidad.fechaCierreEstimada());
        return oportunidadesRespositoryPort.save(newoportunidad);
    }

    @Override
    public Oportunidad cambiarEtapa(Long id, EtapaOportunidad nuevaEtapa) {
        Oportunidad oportunidad = oportunidadesRespositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Oportunidad no encontrada con id: " + id));
        oportunidad.setEtapaOportunidad(nuevaEtapa);
        return oportunidadesRespositoryPort.save(oportunidad);
    }

    @Override
    public Oportunidad ajustarProbabilidad(Long id, int probabilidad) {
        Oportunidad oportunidad = oportunidadesRespositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Oportunidad no encontrada con id: " + id));
        oportunidad.setProbabilidad(probabilidad);
        return oportunidadesRespositoryPort.save(oportunidad);
    }

    @Override
    public void cerrarComoGanada(Long id) {
        Oportunidad oportunidad = oportunidadesRespositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Oportunidad no encontrada con id: " + id));
        oportunidad.cerrarComoGanada();
        oportunidadesRespositoryPort.save(oportunidad);
    }

    @Override
    public void cerrarComoPerdida(Long id) {
        Oportunidad oportunidad = oportunidadesRespositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Oportunidad no encontrada con id: " + id));
        oportunidad.cerrarComoPerdida();
        oportunidadesRespositoryPort.save(oportunidad);
    }
}
