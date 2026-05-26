package com.terreneitors.backendclintec.interactions.application.service;

import com.terreneitors.backendclintec.clients.application.port.out.ClientRepositoryPort;
import com.terreneitors.backendclintec.clients.domain.Client;
import com.terreneitors.backendclintec.interactions.application.port.out.InteractionRepositoryPort;
import com.terreneitors.backendclintec.interactions.domain.Interaction;
import com.terreneitors.backendclintec.interactions.domain.TypeInteraction;
import com.terreneitors.backendclintec.interactions.infrastructure.dto.InteractionRequestDTO;
import com.terreneitors.backendclintec.opportunities.application.port.out.OpportunityRepositoryPort;
import com.terreneitors.backendclintec.opportunities.domain.Opportunity;
import com.terreneitors.backendclintec.opportunities.domain.StatusOpportunity;
import com.terreneitors.backendclintec.users.application.port.out.UserRepositoryPort;
import com.terreneitors.backendclintec.users.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InteractionCrudServiceTest {

    @Mock
    private InteractionRepositoryPort interactionRepositoryPort;

    @Mock
    private ClientRepositoryPort clientRepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private OpportunityRepositoryPort opportunityRepositoryPort;

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private InteractionCrudService interactionCrudService;

    private Interaction interaccionExistente;
    private Opportunity oportunidadActiva;
    private Opportunity oportunidadPerdida;
    private InteractionRequestDTO dto;
    private Client clienteExistente;
    private User usuarioExistente;

    @BeforeEach
    void setUp() {
        clienteExistente = new Client();
        clienteExistente.setId(10L);

        usuarioExistente = new User();
        usuarioExistente.setId(5L);

        oportunidadActiva = new Opportunity();
        oportunidadActiva.setIdOportunidad(20L);
        oportunidadActiva.setClienteId(10L);
        oportunidadActiva.setEstado(StatusOpportunity.ACTIVA);

        oportunidadPerdida = new Opportunity();
        oportunidadPerdida.setIdOportunidad(20L);
        oportunidadPerdida.setClienteId(10L);
        oportunidadPerdida.setEstado(StatusOpportunity.PERDIDA);

        interaccionExistente = new Interaction();
        interaccionExistente.setId(1L);
        interaccionExistente.setClienteId(10L);
        interaccionExistente.setUsuarioId(5L);
        interaccionExistente.setOportunidadId(20L);
        interaccionExistente.setTipo(TypeInteraction.LLAMADA);
        interaccionExistente.setNota("Nota de prueba");

        dto = new InteractionRequestDTO(
                10L,
                5L,
                20L,
                TypeInteraction.LLAMADA,
                "Nota de prueba"
        );
    }

    @Test
    void findAll() {
        when(interactionRepositoryPort.findAll())
                .thenReturn(List.of(interaccionExistente));

        List<Interaction> resultado = interactionCrudService.findAll();

        assertThat(resultado).hasSize(1);

    }

    @Test
    void findById() {
        when(interactionRepositoryPort.findById(1L))
                .thenReturn(Optional.of(interaccionExistente));

        Optional<Interaction> resultado = interactionCrudService.findById(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(1L);
    }


    @Test
    void createInteraction() {
        when(clientRepositoryPort.findById(10L))
                .thenReturn(Optional.of(clienteExistente));
        when(userRepositoryPort.findById(5L))
                .thenReturn(Optional.of(usuarioExistente));
        when(opportunityRepositoryPort.findById(20L))
                .thenReturn(Optional.of(oportunidadActiva));
        when(interactionRepositoryPort.save(any(Interaction.class)))
                .thenReturn(interaccionExistente);

        Interaction resultado = interactionCrudService.createInteraction(dto);

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(interactionRepositoryPort).save(any(Interaction.class));
    }

    @Test
    void updateInteraction() {
        when(interactionRepositoryPort.findById(1L))
                .thenReturn(Optional.of(interaccionExistente));
        when(interactionRepositoryPort.save(any(Interaction.class)))
                .thenReturn(interaccionExistente);

        Interaction resultado = interactionCrudService.updateInteraction(1L, dto);

        assertThat(resultado).isNotNull();
        verify(interactionRepositoryPort).save(any(Interaction.class));
    }
}