package com.terreneitors.backendclintec.clients.infrastructure.persistence.mapper;

import com.terreneitors.backendclintec.clients.domain.Client;
import com.terreneitors.backendclintec.clients.infrastructure.dto.ClientResponseDTO;
import com.terreneitors.backendclintec.clients.infrastructure.persistence.ClientEntity;
import org.springframework.stereotype.Component;

@Component
public class ClientPersistenceMapper {

    public Client toDomain(ClientEntity entity) {
        if (entity == null) return null;
        Client client = new Client();
        client.setId(entity.getId());
        client.setNombreCliente(entity.getNombreCliente());
        client.setEmail(entity.getEmail());
        client.setEmpresa(entity.getEmpresa());
        client.setDireccion(entity.getDireccion());
        client.setTelefono(entity.getTelefono());
        client.setFechaRegistro(entity.getFechaRegistro());
        return client;
    }

    public ClientEntity toEntity(Client client) {
        if (client == null) return null;
        ClientEntity entity = new ClientEntity();
        entity.setId(client.getId());
        entity.setNombreCliente(client.getNombreCliente());
        entity.setEmail(client.getEmail());
        entity.setEmpresa(client.getEmpresa());
        entity.setDireccion(client.getDireccion());
        entity.setTelefono(client.getTelefono());
        entity.setFechaRegistro(client.getFechaRegistro());
        return entity;
    }

    public ClientResponseDTO toDTO(Client u){
        if (u == null) return  null;
        return new ClientResponseDTO(
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
