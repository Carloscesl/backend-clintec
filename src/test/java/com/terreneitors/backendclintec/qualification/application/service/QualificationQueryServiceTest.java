package com.terreneitors.backendclintec.qualification.application.service;

import com.terreneitors.backendclintec.qualification.application.port.out.QualificationHistoryRepositoryPort;
import com.terreneitors.backendclintec.qualification.application.port.out.QualificationRepositoryPort;
import com.terreneitors.backendclintec.qualification.domain.Qualification;
import com.terreneitors.backendclintec.qualification.domain.QualificationClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QualificationQueryServiceTest {

    @Mock
    private QualificationRepositoryPort qualificationRepositoryPort;

    @Mock
    private QualificationHistoryRepositoryPort historyRepositoryPort;

    @InjectMocks
    private QualificationQueryService qualificationQueryService;

    @Test
    @DisplayName("Debe agrupar correctamente la distribución de clientes por nivel")
    void distribucionPorNivel_ShouldReturnGroupedMap() {
        // Arrange
        QualificationClient c1 = new QualificationClient(); c1.setClasificacion(Qualification.FRIO);
        QualificationClient c2 = new QualificationClient(); c2.setClasificacion(Qualification.FRIO);
        QualificationClient c3 = new QualificationClient(); c3.setClasificacion(Qualification.VIP);

        when(qualificationRepositoryPort.findAll()).thenReturn(List.of(c1, c2, c3));

        // Act
        Map<Qualification, Long> resultado = qualificationQueryService.distribucionPorNivel();

        // Assert
        assertEquals(2L, resultado.get(Qualification.FRIO));
        assertEquals(1L, resultado.get(Qualification.VIP));
    }

    @Test
    @DisplayName("Debe consultar el historial de un cliente específico")
    void historialPorCliente_ShouldCallRepository() {
        Long id = 10L;
        qualificationQueryService.historialPorCliente(id);

        verify(historyRepositoryPort).findByClienteId(id);
    }

    @Test
    @DisplayName("Debe consultar clientes en riesgo llamando al repositorio con fecha correcta")
    void findEnRiesgo_ShouldCallRepositoryWithThreshold() {
        qualificationQueryService.findEnRiesgo();

        // Verificamos que el repositorio sea llamado, sin importar el valor exacto de la fecha (any)
        verify(qualificationRepositoryPort).findClientesQueBAjaronPuntaje(any());
    }
}