package com.terreneitors.backendclintec.interactions.infrastructure.persistence;

import com.terreneitors.backendclintec.interactions.application.port.out.InteractionRepositoryPort;
import com.terreneitors.backendclintec.interactions.domain.Interaction;
import com.terreneitors.backendclintec.interactions.infrastructure.persistence.Mapper.InteractionPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
    public class InteractionRepositoryAdapter implements InteractionRepositoryPort {
    private final SpringInteractionRepository springInteractionRepository;
    private final InteractionPersistenceMapper mapper;

    @Override
    public Interaction save(Interaction interaccion) {
        return  mapper.toDomain(springInteractionRepository.save(mapper.toEntity(interaccion)));
    }

    @Override
    public Optional<Interaction> findById(Long id) {
        return springInteractionRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Interaction> findAll() {
        return springInteractionRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Interaction> findByClienteId(Long clienteId) {
        return springInteractionRepository.findByClienteId(clienteId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Interaction> findByOportunidadId(Long oportunidadId) {
        return springInteractionRepository.findByOportunidadId(oportunidadId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Interaction> findByUsuarioId(Long usuarioId) {
        return springInteractionRepository.findByUsuarioId(usuarioId).stream().map(mapper::toDomain).toList();
    }
}
