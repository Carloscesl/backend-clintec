package com.terreneitors.backendclintec.clients.application.service;

import com.terreneitors.backendclintec.clients.application.port.out.ClientRepositoryPort;
import com.terreneitors.backendclintec.clients.domain.Client;
import com.terreneitors.backendclintec.clients.infrastructure.dto.ClientRequestDTO;
import com.terreneitors.backendclintec.qualification.application.port.in.QualificationCrudUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientCrudServiceTest {

    @Mock
    private ClientRepositoryPort clientRepositoryPort;

    @Mock
    private QualificationCrudUseCase qualificationCrudUseCase;

    @InjectMocks
    private ClientCrudService clientCrudService;

    private Client clienteExistente;
    private ClientRequestDTO dto;

    @BeforeEach
    void setUp() {
        clienteExistente = new Client();
        clienteExistente.setId(1L);
        clienteExistente.setNombreCliente("Carlos López");
        clienteExistente.setEmail("carlos@mail.com");
        clienteExistente.setEmpresa("Empresa S.A.");
        clienteExistente.setDireccion("Calle 123");
        clienteExistente.setTelefono("3001234567");

        dto = new ClientRequestDTO(
                "Carlos López",
                "Empresa S.A.",
                "carlos@mail.com",
                "Calle 123",
                "3001234567"
        );
    }

    @Test
    void findAll() {

        when(clientRepositoryPort.findAll())
                .thenReturn(List.of(clienteExistente));

        List<Client> resultado = clientCrudService.findAll();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEmail()).isEqualTo("carlos@mail.com");

    }

    @Test
    void findById() {
        when(clientRepositoryPort.findById(1L)).thenReturn(Optional.of(clienteExistente));

        Optional<Client> resultado = clientCrudService.findById(1l);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(1L);
    }

    @Test
    void findByEmail() {
        when(clientRepositoryPort.findByEmail("carlos@mail.com")).thenReturn(Optional.of(clienteExistente));

        Optional<Client> resultado = clientCrudService.findByEmail("carlos@mail.com");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getEmail()).isEqualTo("carlos@mail.com");
    }

    @Test
    void createClient() {
        when(clientRepositoryPort.findByEmail("carlos@mail.com")).thenReturn(Optional.empty());

        when(clientRepositoryPort.save(any(Client.class))).thenReturn(clienteExistente);

        Client resultado = clientCrudService.createClient(dto);

        assertThat(resultado.getEmail()).isEqualTo("carlos@mail.com");

        verify(clientRepositoryPort).save(any(Client.class));
        verify(qualificationCrudUseCase).createQualificationInitial(1l);

    }

    @Test
    void updateClient() {
        when(clientRepositoryPort.findById(1L))
                .thenReturn(Optional.of(clienteExistente));

        when(clientRepositoryPort.save(any(Client.class)))
                .thenReturn(clienteExistente);

        Client resultado = clientCrudService.updateClient(1L, dto);

        assertThat(resultado.getNombreCliente()).isEqualTo("Carlos López");
        verify(clientRepositoryPort).save(any(Client.class));
    }

    @Test
    void count() {
        when(clientRepositoryPort.count()).thenReturn(5L);

        Long resultado = clientCrudService.count();

        assertThat(resultado).isEqualTo(5L);
    }
}