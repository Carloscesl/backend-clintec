package com.terreneitors.backendclintec.clients.application.port.out;

import com.terreneitors.backendclintec.clients.domain.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepositoryPort {
    Optional<Client> findByEmail(String email);
    Optional<Client> findById(Long id);
    Client save(Client client);
    List<Client> findAll();
}
