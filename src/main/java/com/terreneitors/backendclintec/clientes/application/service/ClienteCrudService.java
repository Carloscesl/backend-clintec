package com.terreneitors.backendclintec.clientes.application.service;

import com.terreneitors.backendclintec.clientes.application.port.in.ClienteCrudUseCase;
import com.terreneitors.backendclintec.clientes.application.port.out.ClienteRepositoryPort;
import com.terreneitors.backendclintec.clientes.domain.Cliente;
import com.terreneitors.backendclintec.clientes.infrastructure.dto.ClienteRequestDTO;
import com.terreneitors.backendclintec.shared.exception.ResourceNotFoundException;
import com.terreneitors.backendclintec.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClienteCrudService implements ClienteCrudUseCase {

    private final ClienteRepositoryPort clienteRepositoryPort;

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
    public Cliente crearCliente(ClienteRequestDTO dto) {

        if(clienteRepositoryPort.findByEmail(dto.email()).isPresent()){
            log.warn("[CLIENTE_EMAIL_DUPLICADO] email={}", dto.email());
            throw new ValidationException("El correo ya está registrado: " + dto.email());
        }
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setNombreCliente(dto.nombreCliente());
        nuevoCliente.setEmail(dto.email());
        nuevoCliente.setEmpresa(dto.empresa());
        nuevoCliente.setDireccion(dto.direccion());
        nuevoCliente.setTelefono(dto.telefono());

        Cliente guardado = clienteRepositoryPort.save(nuevoCliente);
        log.info("[CLIENTE_CREADO] id={} | email={}", guardado.getId(), guardado.getEmail());

        return guardado;
    }

    @Override
    public Cliente actualizarCliente(Long id, ClienteRequestDTO dto) {
        log.info("[CLIENTE_ACTUALIZAR] id={}", id);

        Cliente cliente = clienteRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "id", id));

        cliente.setNombreCliente(dto.nombreCliente());
        cliente.setEmail(dto.email());
        cliente.setEmpresa(dto.empresa());
        cliente.setDireccion(dto.direccion());
        cliente.setTelefono(dto.telefono());

        Cliente actualizado = clienteRepositoryPort.save(cliente);
        log.info("[CLIENTE_ACTUALIZADO] id={} | email={}", id, actualizado.getEmail());

        return actualizado;
    }
}
