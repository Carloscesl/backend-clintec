package com.terreneitors.backendclintec.clients.infrastructure.persistence;


import com.terreneitors.backendclintec.clients.application.port.out.ClientRepositoryPort;
import com.terreneitors.backendclintec.clients.domain.Client;
import com.terreneitors.backendclintec.clients.infrastructure.persistence.mapper.ClientPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ClientRepositoryAdapter implements ClientRepositoryPort {
    private final SpringClientRepository springClientRepository;
    private final ClientPersistenceMapper mapper;

    public ClientRepositoryAdapter(SpringClientRepository springClientRepository, ClientPersistenceMapper mapper) {
        this.springClientRepository = springClientRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Client> findByEmail(String email) {
        return springClientRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Optional<Client> findById(Long id) {
        return springClientRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Client save(Client client) {
        ClientEntity entity = mapper.toEntity(client);
        ClientEntity saved = springClientRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public List<Client> findAll() {
        return springClientRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
