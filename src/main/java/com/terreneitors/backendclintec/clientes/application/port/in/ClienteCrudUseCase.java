package com.terreneitors.backendclintec.clientes.application.port.in;

import com.terreneitors.backendclintec.clientes.domain.Cliente;
import com.terreneitors.backendclintec.clientes.infrastructure.dto.ClienteRequestDTO;

import java.util.List;
import java.util.Optional;

public interface ClienteCrudUseCase {
    List<Cliente> findAll();
    Optional<Cliente> buscarPorId(Long id);
    Optional<Cliente> buscarPorEmail(String email);

    Cliente crearCliente (ClienteRequestDTO cliente);

    Cliente actualizarCliente(Long id, ClienteRequestDTO cliente);
}
