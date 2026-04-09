package com.terreneitors.backendclintec.oportunidades.application.port.out;

import com.terreneitors.backendclintec.oportunidades.domain.Oportunidades;
import com.terreneitors.backendclintec.oportunidades.infrastructure.dto.OportunidadesRequestDTO;

import java.util.List;
import java.util.Optional;

public interface OportunidadesRespositoryPort {
    List<Oportunidades> findAll();

    Optional<Oportunidades> findById(Long id);
    Optional<Oportunidades> findByAsesor(Long id);
    Optional<Oportunidades> findByIdCliente(Long id);

    Oportunidades save (Oportunidades oportunidad);
}
