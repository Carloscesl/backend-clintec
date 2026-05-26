package com.terreneitors.backendclintec.qualification.application.service;

import com.terreneitors.backendclintec.clients.application.port.out.ClientRepositoryPort;
import com.terreneitors.backendclintec.clients.domain.Client;
import com.terreneitors.backendclintec.qualification.application.port.out.QualificationRepositoryPort;
import com.terreneitors.backendclintec.qualification.domain.QualificationClient;
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
class QualificationCrudServiceTest {

    @Mock
    private QualificationRepositoryPort qualificationRepositoryPort;

    @Mock
    private ClientRepositoryPort clientRepositoryPort;

    @InjectMocks
    private QualificationCrudService qualificationCrudService;

    private QualificationClient calificacionExistente;
    private Client clienteExistente;

    @BeforeEach
    void setUp() {
        clienteExistente = new Client();
        clienteExistente.setId(1L);

        calificacionExistente = new QualificationClient();
        calificacionExistente.setId(1L);
        calificacionExistente.setPuntaje(50);
    }

    @Test
    void createQualificationInitial() {

        when(qualificationRepositoryPort.save(any(QualificationClient.class)))
                .thenReturn(calificacionExistente);

        QualificationClient resultado =
                qualificationCrudService.createQualificationInitial(1L);

        assertThat(resultado.getId()).isEqualTo(1L);

        verify(qualificationRepositoryPort).save(any(QualificationClient.class));
    }

    @Test
    void findByClientId() {
        when(qualificationRepositoryPort.findByClientId(1L))
                .thenReturn(Optional.of(calificacionExistente));

        Optional<QualificationClient> resultado =
                qualificationCrudService.findByClientId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(1L);
    }

    @Test
    void findAll() {
        when(qualificationRepositoryPort.findAll())
                .thenReturn(List.of(calificacionExistente));

        List<QualificationClient> resultado =
                qualificationCrudService.findAll();

        assertThat(resultado).hasSize(1);
    }

    @Test
    void updateScore() {
        when(qualificationRepositoryPort.findByClientId(1L))
                .thenReturn(Optional.of(calificacionExistente));
        when(qualificationRepositoryPort.save(any(QualificationClient.class)))
                .thenReturn(calificacionExistente);

        QualificationClient resultado =
                qualificationCrudService.updateScore(1L, 80);

        assertThat(resultado).isNotNull();
        verify(qualificationRepositoryPort).save(any(QualificationClient.class));
    }

}