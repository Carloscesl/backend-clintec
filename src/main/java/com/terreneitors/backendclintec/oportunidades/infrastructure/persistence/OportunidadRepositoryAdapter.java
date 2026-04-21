package com.terreneitors.backendclintec.oportunidades.infrastructure.persistence;

import com.terreneitors.backendclintec.oportunidades.application.port.out.OportunidadRespositoryPort;
import com.terreneitors.backendclintec.oportunidades.domain.Oportunidad;
import com.terreneitors.backendclintec.oportunidades.infrastructure.persistence.mapper.OportunidadPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OportunidadRepositoryAdapter implements OportunidadRespositoryPort {
    private final SpringOportunidadesRepository springOportunidadesRepository;
    private final OportunidadPersistenceMapper mapper;

    public OportunidadRepositoryAdapter(SpringOportunidadesRepository springOportunidadesRepository, OportunidadPersistenceMapper mapper) {
        this.springOportunidadesRepository = springOportunidadesRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Oportunidad> findAll() {
        return springOportunidadesRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Oportunidad> findById(Long id) {
        return springOportunidadesRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Oportunidad> findByAsesor(Long id) {
        return springOportunidadesRepository.findByAsesorId(id).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Oportunidad> findByIdCliente(Long id) {
        return springOportunidadesRepository.findByClienteId(id).stream().map(mapper::toDomain).toList();
    }

    @Override
    public Oportunidad save(Oportunidad oportunidad) {
        OportunidadEntity entity = mapper.toEntity(oportunidad);
        OportunidadEntity saved = springOportunidadesRepository.save(entity);
        return mapper.toDomain(saved);
    }
}
