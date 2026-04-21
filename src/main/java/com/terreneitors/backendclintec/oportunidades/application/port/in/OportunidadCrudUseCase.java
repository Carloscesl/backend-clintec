package com.terreneitors.backendclintec.oportunidades.application.port.in;

import com.terreneitors.backendclintec.oportunidades.domain.EtapaOportunidad;
import com.terreneitors.backendclintec.oportunidades.domain.Oportunidad;
import com.terreneitors.backendclintec.oportunidades.infrastructure.dto.OportunidadRequestDTO;

import java.util.List;
import java.util.Optional;

public interface OportunidadCrudUseCase {
    List<Oportunidad> findAll();

    Optional<Oportunidad> buscarPorId(Long id);
    List<Oportunidad> buscarPorIdAsesor(Long id);
    List<Oportunidad> buscarPorIdCliente(Long id);

    Oportunidad crearOportunidades (OportunidadRequestDTO oportunidad);
    Oportunidad actulizarOportunidades (Long id, OportunidadRequestDTO oportunidad);
    Oportunidad cambiarEtapa(Long id, EtapaOportunidad nuevaEtapa);
    Oportunidad ajustarProbabilidad(Long id, int probabilidad);
    void  cerrarComoGanada(Long id);
    void  cerrarComoPerdida(Long id);
}
