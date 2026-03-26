package com.terreneitors.backendclintec.clientes.infrastructure.persistence;


import com.terreneitors.backendclintec.clientes.application.port.out.ClienteRepositoryPort;
import com.terreneitors.backendclintec.clientes.domain.Cliente;
import com.terreneitors.backendclintec.clientes.infrastructure.persistence.mapper.ClientePersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ClienteRepositoryAdapter implements ClienteRepositoryPort {
    private final SpringClienteRepository springClienteRepository;
    private final ClientePersistenceMapper mapper;

    public ClienteRepositoryAdapter(SpringClienteRepository springClienteRepository, ClientePersistenceMapper mapper) {
        this.springClienteRepository = springClienteRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Cliente> findByEmail(String email) {
        return springClienteRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return springClienteRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Cliente save(Cliente cliente) {
        ClienteEntity entity = mapper.toEntity(cliente);
        ClienteEntity saved = springClienteRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public List<Cliente> findAll() {
        return springClienteRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
