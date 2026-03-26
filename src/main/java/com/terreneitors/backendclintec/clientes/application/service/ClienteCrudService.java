package com.terreneitors.backendclintec.clientes.application.service;

import com.terreneitors.backendclintec.clientes.application.port.in.ClienteCrudUseCase;
import com.terreneitors.backendclintec.clientes.application.port.out.ClienteRepositoryPort;
import com.terreneitors.backendclintec.clientes.domain.Cliente;
import com.terreneitors.backendclintec.clientes.infrastructure.dto.ClienteRequestDTO;
import com.terreneitors.backendclintec.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteCrudService implements ClienteCrudUseCase {
    private final ClienteRepositoryPort clienteRepositoryPort;

    public ClienteCrudService(ClienteRepositoryPort clienteRepositoryPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
    }

    @Override
    public List<Cliente> findAll() {
        return clienteRepositoryPort.findAll();
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepositoryPort.findById(id);
    }

    @Override
    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepositoryPort.findByEmail(email);
    }

    @Override
    public Cliente crearCliente(ClienteRequestDTO cliente) {
        if(clienteRepositoryPort.findByEmail(cliente.email()).isPresent()){
            throw new RuntimeException("El correo ya esta registrado");
        }
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setNombreCliente(cliente.nombreCliente());
        nuevoCliente.setEmail(cliente.email());
        nuevoCliente.setEmpresa(cliente.empresa());
        nuevoCliente.setDireccion(cliente.direccion());
        nuevoCliente.setTelefono(cliente.telefono());
        nuevoCliente.setFechaRegistro(LocalDateTime.now());

        return clienteRepositoryPort.save(nuevoCliente);
    }

    @Override
    public Cliente actualizarCliente(Long id, ClienteRequestDTO dto) {
        return  clienteRepositoryPort.findById(id).map(u -> {
            u.setNombreCliente(dto.nombreCliente());
            u.setEmail(dto.email());
            u.setEmpresa(dto.empresa());
            u.setDireccion(dto.direccion());
            u.setTelefono(dto.telefono());
            return clienteRepositoryPort.save(u);
        }).orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
    }
}
