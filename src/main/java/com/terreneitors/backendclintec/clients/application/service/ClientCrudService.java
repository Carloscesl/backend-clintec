package com.terreneitors.backendclintec.clients.application.service;

import com.terreneitors.backendclintec.clients.application.port.in.ClientCrudUseCase;
import com.terreneitors.backendclintec.clients.application.port.out.ClientRepositoryPort;
import com.terreneitors.backendclintec.clients.domain.Client;
import com.terreneitors.backendclintec.clients.infrastructure.dto.ClientRequestDTO;
import com.terreneitors.backendclintec.qualification.application.port.in.QualificationCrudUseCase;
import com.terreneitors.backendclintec.shared.exception.ResourceNotFoundException;
import com.terreneitors.backendclintec.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientCrudService implements ClientCrudUseCase {

    private final ClientRepositoryPort clientRepositoryPort;
    private final QualificationCrudUseCase qualificationCrudUseCase;

    @Override
    public List<Client> findAll() {
        return clientRepositoryPort.findAll();
    }

    @Override
    public Optional<Client> findById(Long id) {
        return clientRepositoryPort.findById(id);
    }

    @Override
    public Optional<Client> findByEmail(String email) {
        return clientRepositoryPort.findByEmail(email);
    }

    @Override
    public Client createClient(ClientRequestDTO dto) {

        if(clientRepositoryPort.findByEmail(dto.email()).isPresent()){
            log.warn("[CLIENTE_EMAIL_DUPLICADO] email={}", dto.email());
            throw new ValidationException("El correo ya está registrado: " + dto.email());
        }
        Client nuevoClient = new Client();
        nuevoClient.setNombreCliente(dto.nombreCliente());
        nuevoClient.setEmail(dto.email());
        nuevoClient.setEmpresa(dto.empresa());
        nuevoClient.setDireccion(dto.direccion());
        nuevoClient.setTelefono(dto.telefono());

        Client guardado = clientRepositoryPort.save(nuevoClient);
        log.info("[CLIENTE_CREADO] id={} | email={}", guardado.getId(), guardado.getEmail());

        qualificationCrudUseCase.createQualificationInitial(guardado.getId());

        return guardado;
    }

    @Override
    public Client updateClient(Long id, ClientRequestDTO dto) {
        log.info("[CLIENTE_ACTUALIZAR] id={}", id);

        Client client = clientRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "id", id));

        client.setNombreCliente(dto.nombreCliente());
        client.setEmail(dto.email());
        client.setEmpresa(dto.empresa());
        client.setDireccion(dto.direccion());
        client.setTelefono(dto.telefono());

        Client actualizado = clientRepositoryPort.save(client);
        log.info("[CLIENTE_ACTUALIZADO] id={} | email={}", id, actualizado.getEmail());

        return actualizado;
    }
}
