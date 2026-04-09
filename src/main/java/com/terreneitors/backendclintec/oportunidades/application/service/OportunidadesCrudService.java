package com.terreneitors.backendclintec.oportunidades.application.service;

import com.terreneitors.backendclintec.oportunidades.application.port.in.OportunidadesCrudUseCase;
import com.terreneitors.backendclintec.oportunidades.application.port.out.OportunidadesRespositoryPort;
import com.terreneitors.backendclintec.oportunidades.domain.Oportunidades;
import com.terreneitors.backendclintec.oportunidades.infrastructure.dto.OportunidadesRequestDTO;
import com.terreneitors.backendclintec.shared.exception.ResourceNotFoundException;
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
    public List<Oportunidades> findAll() {
        return oportunidadesRespositoryPort.findAll();
    }

    @Override
    public Optional<Oportunidades> buscarPorId(Long id) {
        return oportunidadesRespositoryPort.findById(id);
    }

    @Override
    public Optional<Oportunidades> buscarPorIdAsesor(Long id) {
        return oportunidadesRespositoryPort.findByAsesor(id);
    }

    @Override
    public Optional<Oportunidades> buscarPorIdCliente(Long id) {
        return oportunidadesRespositoryPort.findByIdCliente(id);
    }

    @Override
    public Oportunidades crearOportunidades(OportunidadesRequestDTO oportunidad) {
        Oportunidades nueva = new Oportunidades();
        nueva.setClienteId(oportunidad.clienteId());
        nueva.setAsesorId(oportunidad.asesorId());
        nueva.setEstado(oportunidad.estado());
        nueva.setDescripcion(oportunidad.descripcion());
        nueva.setValorEstimado(oportunidad.valorEstimado());
        nueva.setFechaCreacion(LocalDateTime.now());
        return oportunidadesRespositoryPort.save(nueva);
    }

    @Override
    public Oportunidades actulizarOportunidades(Long id, OportunidadesRequestDTO oportunidad) {
        return oportunidadesRespositoryPort.findById(id).map( u -> {
            u.setClienteId(oportunidad.clienteId());
            u.setAsesorId(oportunidad.asesorId());
            u.setEstado(oportunidad.estado());
            u.setDescripcion(oportunidad.descripcion());
            u.setValorEstimado(oportunidad.valorEstimado());
            return oportunidadesRespositoryPort.save(u);
        }).orElseThrow(() -> new ResourceNotFoundException("Oportunidad no encontrada"));
    }
}
