package com.terreneitors.backendclintec.clientes.infrastructure.persistence.mapper;

import com.terreneitors.backendclintec.clientes.domain.Cliente;
import com.terreneitors.backendclintec.clientes.infrastructure.dto.ClienteResponseDTO;
import com.terreneitors.backendclintec.clientes.infrastructure.persistence.ClienteEntity;
import org.springframework.stereotype.Component;

@Component
public class ClientePersistenceMapper {

    public Cliente toDomain(ClienteEntity entity) {
        if (entity == null) return null;
        Cliente cliente = new Cliente();
        cliente.setId(entity.getId());
        cliente.setNombreCliente(entity.getNombreCliente());
        cliente.setEmail(entity.getEmail());
        cliente.setEmpresa(entity.getEmpresa());
        cliente.setDireccion(entity.getDireccion());
        cliente.setTelefono(entity.getTelefono());
        cliente.setFechaRegistro(entity.getFechaRegistro());
        return cliente;
    }

    public ClienteEntity toEntity(Cliente cliente) {
        if (cliente == null) return null;
        ClienteEntity entity = new ClienteEntity();
        entity.setId(cliente.getId());
        entity.setNombreCliente(cliente.getNombreCliente());
        entity.setEmail(cliente.getEmail());
        entity.setEmpresa(cliente.getEmpresa());
        entity.setDireccion(cliente.getDireccion());
        entity.setTelefono(cliente.getTelefono());
        entity.setFechaRegistro(cliente.getFechaRegistro());
        return entity;
    }

    public ClienteResponseDTO toDTO(Cliente u){
        if (u == null) return  null;
        return new ClienteResponseDTO(
                u.getId(),
                u.getNombreCliente(),
                u.getEmpresa(),
                u.getEmail(),
                u.getTelefono(),
                u.getDireccion(),
                u.getFechaRegistro()
        );
    }
}
