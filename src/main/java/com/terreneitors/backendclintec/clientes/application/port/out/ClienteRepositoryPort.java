package com.terreneitors.backendclintec.clientes.application.port.out;

import com.terreneitors.backendclintec.clientes.domain.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepositoryPort {
    Optional<Cliente> findByEmail(String email);
    Optional<Cliente> findById(Long id);
    Cliente save(Cliente cliente);
    List<Cliente> findAll();
}
