package com.terreneitors.backendclintec.oportunidades.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringOportunidadesRepository extends JpaRepository<OportunidadEntity, Long> {
    List<OportunidadEntity> findByAsesorId(Long vendedorId);
    List<OportunidadEntity> findByClienteId(Long clienteId);
}
