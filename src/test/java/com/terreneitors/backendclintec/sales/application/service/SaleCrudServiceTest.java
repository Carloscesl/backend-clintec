package com.terreneitors.backendclintec.sales.application.service;

import com.terreneitors.backendclintec.opportunities.application.port.out.OpportunityRepositoryPort;
import com.terreneitors.backendclintec.opportunities.domain.Opportunity;
import com.terreneitors.backendclintec.opportunities.domain.StatusOpportunity;
import com.terreneitors.backendclintec.sales.application.port.out.SaleRepositoryPort;
import com.terreneitors.backendclintec.sales.domain.PaymentMethod;
import com.terreneitors.backendclintec.sales.domain.Sale;
import com.terreneitors.backendclintec.sales.infrastructure.dto.SaleRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaleCrudServiceTest {

    @Mock
    private SaleRepositoryPort saleRepositoryPort;

    @Mock
    private OpportunityRepositoryPort opportunityRepositoryPort;

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private SaleCrudService saleCrudService;

    private Sale ventaExistente;
    private Opportunity oportunidadGanada;
    private Opportunity oportunidadPerdida;
    private SaleRequestDTO dto;

    @BeforeEach
    void setUp() {
        ventaExistente = new Sale();
        ventaExistente.setIdVenta(1L);
        ventaExistente.setIdOportunidad(10L);
        ventaExistente.setIdAsesor(2L);
        ventaExistente.setValorVenta(BigDecimal.valueOf(5000.0));
        ventaExistente.setNotas("Venta exitosa");

        oportunidadGanada = new Opportunity();
        oportunidadGanada.setIdOportunidad(10L);
        oportunidadGanada.setEstado(StatusOpportunity.GANADA);

        oportunidadPerdida = new Opportunity();
        oportunidadPerdida.setIdOportunidad(10L);
        oportunidadPerdida.setEstado(StatusOpportunity.PERDIDA);

        dto = new SaleRequestDTO(
                10L,
                2L,
                BigDecimal.valueOf(5000.0),
                "Venta exitosa",
                PaymentMethod.EFECTIVO
        );
    }

    @Test
    void findAll() {
        when(saleRepositoryPort.findAll())
                .thenReturn(List.of(ventaExistente));

        List<Sale> resultado = saleCrudService.findAll();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.getFirst().getIdVenta()).isEqualTo(1L);
    }

    @Test
    void findId() {
        when(saleRepositoryPort.findId(1L))
                .thenReturn(Optional.of(ventaExistente));

        Optional<Sale> resultado = saleCrudService.findId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getIdVenta()).isEqualTo(1L);
    }

    @Test
    void findIdAssessor() {
        when(saleRepositoryPort.findIdAssessor(2L))
                .thenReturn(List.of(ventaExistente));

        List<Sale> resultado = saleCrudService.findIdAssessor(2L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.getFirst().getIdAsesor()).isEqualTo(2L);
    }

    @Test
    void findIdOpportunity() {
        when(saleRepositoryPort.findIdOpportunity(10L))
                .thenReturn(List.of(ventaExistente));

        List<Sale> resultado = saleCrudService.findIdOpportunity(10L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.getFirst().getIdOportunidad()).isEqualTo(10L);
    }

    @Test
    void createSale() {
        when(opportunityRepositoryPort.findById(10L))
                .thenReturn(Optional.of(oportunidadGanada));
        when(saleRepositoryPort.saveSale(any(Sale.class)))
                .thenReturn(ventaExistente);

        Sale resultado = saleCrudService.createSale(dto);

        assertThat(resultado.getIdVenta()).isEqualTo(1L);
        assertThat(resultado.getValorVenta()).isEqualByComparingTo(BigDecimal.valueOf(5000));
        verify(saleRepositoryPort).saveSale(any(Sale.class));
    }

    @Test
    void updateSale() {
        when(saleRepositoryPort.findId(1L))
                .thenReturn(Optional.of(ventaExistente));
        when(saleRepositoryPort.saveSale(any(Sale.class)))
                .thenReturn(ventaExistente);

        Sale resultado = saleCrudService.updateSale(1L, dto);

        assertThat(resultado).isNotNull();
        verify(saleRepositoryPort).saveSale(any(Sale.class));
    }

    @Test
    void count() {
        when(saleRepositoryPort.count()).thenReturn(7L);

        Long resultado = saleCrudService.count();

        assertThat(resultado).isEqualTo(7L);
    }
}