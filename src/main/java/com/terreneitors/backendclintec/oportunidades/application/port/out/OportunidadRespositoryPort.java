package com.terreneitors.backendclintec.oportunidades.application.port.out;

import com.terreneitors.backendclintec.oportunidades.domain.Oportunidad;

import java.util.List;
import java.util.Optional;

public interface OportunidadRespositoryPort {
    List<Oportunidad> findAll();

    Optional<Oportunidad> findById(Long id);
    List<Oportunidad> findByAsesor(Long id);
    List<Oportunidad> findByIdCliente(Long id);

    Oportunidad save (Oportunidad oportunidad);
}
