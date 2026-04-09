package com.terreneitors.backendclintec.oportunidades.application.port.in;

import com.terreneitors.backendclintec.oportunidades.domain.Oportunidades;
import com.terreneitors.backendclintec.oportunidades.infrastructure.dto.OportunidadesRequestDTO;

import java.util.List;
import java.util.Optional;

public interface OportunidadesCrudUseCase {
    List<Oportunidades> findAll();

    Optional<Oportunidades> buscarPorId(Long id);
    Optional<Oportunidades> buscarPorIdAsesor(Long id);
    Optional<Oportunidades> buscarPorIdCliente(Long id);

    Oportunidades crearOportunidades (OportunidadesRequestDTO oportunidad);

    Oportunidades actulizarOportunidades (Long id,OportunidadesRequestDTO oportunidad);
}
