package com.terreneitors.backendclintec.alerts.application.service;

import com.terreneitors.backendclintec.alerts.application.port.out.AlertRepositoryPort;
import com.terreneitors.backendclintec.alerts.domain.Alert;
import com.terreneitors.backendclintec.alerts.domain.StateAlert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlertCrudServiceTest {

    @Mock
    private AlertRepositoryPort alertRepositoryPort;

    @InjectMocks
    private AlertCrudService alertCrudService;

    private Alert alertaPendiente;
    private Alert alertaResuelta;

    @BeforeEach
    void setUp() {
        alertaPendiente = new Alert();
        alertaPendiente.setId(1L);
        alertaPendiente.setClienteId(10L);
        alertaPendiente.setUsuarioId(2L);
        alertaPendiente.setEstado(StateAlert.PENDIENTE);

        alertaResuelta = new Alert();
        alertaResuelta.setId(2L);
        alertaResuelta.setClienteId(10L);
        alertaResuelta.setUsuarioId(2L);
        alertaResuelta.setEstado(StateAlert.RESUELTA);
    }

    @Test
    void findAll() {
        when(alertRepositoryPort.findAll()).thenReturn(List.of(alertaPendiente, alertaResuelta));

        List<Alert> resultado = alertCrudService.findAll();

        assertThat(resultado).hasSize(2);
    }

    @Test
    void buscarPorId() {
        when(alertRepositoryPort.findById(1L))
                .thenReturn(Optional.of(alertaPendiente));

        Optional<Alert> resultado = alertCrudService.buscarPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(1L);
    }

    @Test
    void buscarPorCliente() {
        when(alertRepositoryPort.findByClienteId(10L))
                .thenReturn(List.of(alertaPendiente));

        List<Alert> resultado = alertCrudService.buscarPorCliente(10L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.getFirst().getClienteId()).isEqualTo(10L);
    }

    @Test
    void buscarPorUsuario() {
        when(alertRepositoryPort.findByUsuarioId(2L))
                .thenReturn(List.of(alertaPendiente));

        List<Alert> resultado = alertCrudService.buscarPorUsuario(2L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.getFirst().getUsuarioId()).isEqualTo(2L);
    }

    @Test
    void buscarPendientes() {
        when(alertRepositoryPort.findPendientes())
                .thenReturn(List.of(alertaPendiente));

        List<Alert> resultado = alertCrudService.buscarPendientes();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.getFirst().getEstado()).isEqualTo(StateAlert.PENDIENTE);
    }

    @Test
    void marcarComoVista() {
        when(alertRepositoryPort.findById(1L))
                .thenReturn(Optional.of(alertaPendiente));
        when(alertRepositoryPort.save(alertaPendiente))
                .thenReturn(alertaPendiente);

        Alert resultado = alertCrudService.marcarComoVista(1L);

        assertThat(resultado).isNotNull();
        verify(alertRepositoryPort).save(alertaPendiente);
    }

    @Test
    void countByEstado() {

        when(alertRepositoryPort.countByEstado(StateAlert.PENDIENTE))
                .thenReturn(4L);

        long resultado = alertCrudService.countByEstado(StateAlert.PENDIENTE);

        assertThat(resultado).isEqualTo(4L);

    }

    @Test
    void countByUsuarioIdAndEstado() {
        when(alertRepositoryPort.countByUsuarioIdAndEstado(2L, StateAlert.PENDIENTE))
                .thenReturn(2L);

        long resultado = alertCrudService.countByUsuarioIdAndEstado(2L, StateAlert.PENDIENTE);

        assertThat(resultado).isEqualTo(2L);
    }
}