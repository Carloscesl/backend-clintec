package com.terreneitors.backendclintec.opportunities.application.service;

import com.terreneitors.backendclintec.clients.application.port.out.ClientRepositoryPort;
import com.terreneitors.backendclintec.clients.domain.Client;
import com.terreneitors.backendclintec.opportunities.application.port.out.OpportunityRepositoryPort;
import com.terreneitors.backendclintec.opportunities.domain.Opportunity;
import com.terreneitors.backendclintec.opportunities.domain.StageOpportunity;
import com.terreneitors.backendclintec.opportunities.domain.StatusOpportunity;
import com.terreneitors.backendclintec.opportunities.infrastructure.dto.OpportunityRequestDTO;
import com.terreneitors.backendclintec.users.application.port.out.UserRepositoryPort;
import com.terreneitors.backendclintec.users.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpportunityCrudServiceTest {

    @Mock
    private OpportunityRepositoryPort oportunidadesRespositoryPort;

    @Mock
    private ClientRepositoryPort clientRepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private OpportunityCrudService opportunityCrudService;

    private Opportunity oportunidadActiva;
    private Opportunity oportunidadGanada;
    private Opportunity oportunidadPerdida;
    private OpportunityRequestDTO dto;
    private Client clienteExistente;
    private User asesorExistente;

    @BeforeEach
    void setUp() {
        oportunidadActiva = new Opportunity();
        oportunidadActiva.setIdOportunidad(1L);
        oportunidadActiva.setClienteId(10L);
        oportunidadActiva.setAsesorId(2L);
        oportunidadActiva.setEstado(StatusOpportunity.ACTIVA);
        oportunidadActiva.setEtapaOportunidad(StageOpportunity.PROSPECCIÓN);
        oportunidadActiva.setValorEstimado(BigDecimal.valueOf(10000));

        oportunidadGanada = new Opportunity();
        oportunidadGanada.setIdOportunidad(2L);
        oportunidadGanada.setEstado(StatusOpportunity.GANADA);

        oportunidadPerdida = new Opportunity();
        oportunidadPerdida.setIdOportunidad(3L);
        oportunidadPerdida.setEstado(StatusOpportunity.PERDIDA);

        clienteExistente = new Client();
        clienteExistente.setId(10L);

        asesorExistente = new User();
        asesorExistente.setId(2L);

        dto = new OpportunityRequestDTO(
                10L,
                2L,
                "Descripción de prueba",
                BigDecimal.valueOf(10000),
                LocalDate.now().plusMonths(3)
        );
    }

    @Test
    void findAll() {
        when(oportunidadesRespositoryPort.findAll())
                .thenReturn(List.of(oportunidadActiva));

        List<Opportunity> resultado = opportunityCrudService.findAll();

        assertThat(resultado).hasSize(1);
    }

    @Test
    void findById() {
        when(oportunidadesRespositoryPort.findById(1L))
                .thenReturn(Optional.of(oportunidadActiva));

        Optional<Opportunity> resultado = opportunityCrudService.findById(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getIdOportunidad()).isEqualTo(1L);
    }

    @Test
    void createOpportunities() {
        when(clientRepositoryPort.findById(10L))
                .thenReturn(Optional.of(clienteExistente));
        when(userRepositoryPort.findById(2L))
                .thenReturn(Optional.of(asesorExistente));
        when(oportunidadesRespositoryPort.save(any(Opportunity.class)))
                .thenReturn(oportunidadActiva);

        Opportunity resultado = opportunityCrudService.createOpportunities(dto);

        assertThat(resultado.getIdOportunidad()).isEqualTo(1L);
        verify(oportunidadesRespositoryPort).save(any(Opportunity.class));
    }

    @Test
    void updateOpportunities() {
        when(oportunidadesRespositoryPort.findById(1L))
                .thenReturn(Optional.of(oportunidadActiva));
        when(oportunidadesRespositoryPort.save(any(Opportunity.class)))
                .thenReturn(oportunidadActiva);

        Opportunity resultado = opportunityCrudService.updateOpportunities(1L, dto);

        assertThat(resultado).isNotNull();
        verify(oportunidadesRespositoryPort).save(any(Opportunity.class));
    }

    @Test
    void adjustProbability() {
        int min = StageOpportunity.PROSPECCIÓN.getMin();
        int probabilidadValida = min + 1;

        when(oportunidadesRespositoryPort.findById(1L))
                .thenReturn(Optional.of(oportunidadActiva));
        when(oportunidadesRespositoryPort.save(any(Opportunity.class)))
                .thenReturn(oportunidadActiva);

        Opportunity resultado = opportunityCrudService
                .adjustProbability(1L, probabilidadValida);

        assertThat(resultado).isNotNull();
        verify(oportunidadesRespositoryPort).save(any(Opportunity.class));
    }

    @Test
    void closeAsWon() {
        when(oportunidadesRespositoryPort.findById(1L))
                .thenReturn(Optional.of(oportunidadActiva));

        opportunityCrudService.closeAsWon(1L);

        verify(oportunidadesRespositoryPort).save(oportunidadActiva);
    }

    @Test
    void closeAsLost() {
        when(oportunidadesRespositoryPort.findById(1L))
                .thenReturn(Optional.of(oportunidadActiva));

        opportunityCrudService.closeAsLost(1L);

        verify(oportunidadesRespositoryPort).save(oportunidadActiva);
    }

    @Test
    void count() {
        when(oportunidadesRespositoryPort.count()).thenReturn(5L);

        assertThat(opportunityCrudService.count()).isEqualTo(5L);
    }
}