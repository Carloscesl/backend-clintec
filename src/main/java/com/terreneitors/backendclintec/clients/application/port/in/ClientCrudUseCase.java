package com.terreneitors.backendclintec.clients.application.port.in;

import com.terreneitors.backendclintec.clients.domain.Client;
import com.terreneitors.backendclintec.clients.infrastructure.dto.ClientRequestDTO;

import java.util.List;
import java.util.Optional;

public interface ClientCrudUseCase {
    List<Client> findAll();
    Optional<Client> findById(Long id);
    Optional<Client> findByEmail(String email);

    Client createClient(ClientRequestDTO cliente);

    Client updateClient(Long id, ClientRequestDTO cliente);
}
