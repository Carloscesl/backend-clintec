package com.terreneitors.backendclintec.oportunidades.infrastructure.persistence;

import com.terreneitors.backendclintec.oportunidades.application.port.out.OportunidadesRespositoryPort;
import com.terreneitors.backendclintec.oportunidades.domain.Oportunidades;
import com.terreneitors.backendclintec.oportunidades.infrastructure.persistence.mapper.OportunidadesPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OportunidadesRepositoryAdapter implements OportunidadesRespositoryPort {
    private final SpringOportunidadesRepository springOportunidadesRepository;
    private final OportunidadesPersistenceMapper mapper;

    public OportunidadesRepositoryAdapter(SpringOportunidadesRepository springOportunidadesRepository, OportunidadesPersistenceMapper mapper) {
        this.springOportunidadesRepository = springOportunidadesRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Oportunidades> findAll() {
        return springOportunidadesRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Oportunidades> findById(Long id) {
        return springOportunidadesRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Oportunidades> findByAsesor(Long id) {
        return springOportunidadesRepository.findByAsesorId(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Oportunidades> findByIdCliente(Long id) {
        return springOportunidadesRepository.findByClienteId(id).map(mapper::toDomain);
    }

    @Override
    public Oportunidades save(Oportunidades oportunidad) {
        OportunidadesEntity entity = mapper.toEntity(oportunidad);
        OportunidadesEntity saved = springOportunidadesRepository.save(entity);
        return mapper.toDomain(saved);
    }
}
