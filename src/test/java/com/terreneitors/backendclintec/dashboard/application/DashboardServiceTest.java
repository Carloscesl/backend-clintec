package com.terreneitors.backendclintec.dashboard.application;

import com.terreneitors.backendclintec.alerts.application.port.out.AlertRepositoryPort;
import com.terreneitors.backendclintec.alerts.domain.StateAlert;
import com.terreneitors.backendclintec.clients.application.port.out.ClientRepositoryPort;
import com.terreneitors.backendclintec.dashboard.infrastructure.dto.AdminDashboardDTO;
import com.terreneitors.backendclintec.dashboard.infrastructure.dto.AsesorDashboardDTO;
import com.terreneitors.backendclintec.dashboard.infrastructure.dto.GerenteDashboardDTO;
import com.terreneitors.backendclintec.opportunities.application.port.out.OpportunityRepositoryPort;
import com.terreneitors.backendclintec.opportunities.domain.StageOpportunity;
import com.terreneitors.backendclintec.opportunities.domain.StatusOpportunity;
import com.terreneitors.backendclintec.sales.application.port.out.SaleRepositoryPort;
import com.terreneitors.backendclintec.users.application.port.out.UserRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private ClientRepositoryPort clienteRepo;
    @Mock
    private UserRepositoryPort usuarioRepo;
    @Mock
    private OpportunityRepositoryPort opportunityRepo;
    @Mock
    private SaleRepositoryPort ventaRepo;
    @Mock
    private AlertRepositoryPort alertaRepo;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    @DisplayName("Debe retornar el Dashboard de Administrador con métricas correctas")
    void debeRetornarAdminDashboard() {
        // Arrange
        when(clienteRepo.count()).thenReturn(50L);
        when(usuarioRepo.count()).thenReturn(10L);
        when(ventaRepo.count()).thenReturn(30L);
        when(alertaRepo.countByEstado(StateAlert.PENDIENTE)).thenReturn(5L);

        when(opportunityRepo.countByEstado(StatusOpportunity.ACTIVA)).thenReturn(15L);
        when(opportunityRepo.countByEstado(StatusOpportunity.GANADA)).thenReturn(20L);
        when(opportunityRepo.countByEstado(StatusOpportunity.PERDIDA)).thenReturn(5L);
        when(opportunityRepo.sumValorEstimadoByEstado(StatusOpportunity.GANADA)).thenReturn(BigDecimal.valueOf(150000.0));

        when(opportunityRepo.countByEtapaOportunidad(StageOpportunity.PROSPECCION)).thenReturn(4L);
        when(opportunityRepo.countByEtapaOportunidad(StageOpportunity.CALIFICACION)).thenReturn(3L);
        when(opportunityRepo.countByEtapaOportunidad(StageOpportunity.PROPUESTA)).thenReturn(5L);
        when(opportunityRepo.countByEtapaOportunidad(StageOpportunity.NEGOCIACION)).thenReturn(3L);
        when(opportunityRepo.countByEtapaOportunidad(StageOpportunity.CIERRE_GANADO)).thenReturn(20L);
        when(opportunityRepo.countByEtapaOportunidad(StageOpportunity.CIERRE_PERDIDO)).thenReturn(5L);

        // Act
        AdminDashboardDTO dto = dashboardService.getAdminDashboard();

        // Assert
        assertNotNull(dto);
        assertEquals(50L, dto.totalClientes());
        assertEquals(10L, dto.totalUsuarios());
        assertEquals(30L, dto.totalVentas());
        assertEquals(5L, dto.alertasPendientes());
        assertEquals(15L, dto.oportunidadesActivas());
        assertEquals(20L, dto.oportunidadesGanadas());
        assertEquals(5L, dto.oportunidadesPerdidas());
        assertEquals(BigDecimal.valueOf(150000.0), dto.valorOportunidadesGanadas());

        // Embudo
        assertEquals(4L, dto.enProspeccion());
        assertEquals(3L, dto.enCalificacion());
        assertEquals(5L, dto.enPropuesta());
        assertEquals(3L, dto.enNegociacion());
        assertEquals(20L, dto.enCierreGanado());
        assertEquals(5L, dto.enCierrePerdido());
    }

    @Test
    @DisplayName("Debe retornar el Dashboard de Gerente con métricas correctas")
    void debeRetornarGerenteDashboard() {
        // Arrange
        when(clienteRepo.count()).thenReturn(100L);
        when(ventaRepo.count()).thenReturn(60L);

        when(opportunityRepo.countByEstado(StatusOpportunity.ACTIVA)).thenReturn(25L);
        when(opportunityRepo.countByEstado(StatusOpportunity.GANADA)).thenReturn(40L);
        when(opportunityRepo.countByEstado(StatusOpportunity.PERDIDA)).thenReturn(10L);
        when(opportunityRepo.sumValorEstimadoByEstado(StatusOpportunity.GANADA)).thenReturn(BigDecimal.valueOf(300000.0));

        when(opportunityRepo.countByEtapaOportunidad(StageOpportunity.PROSPECCION)).thenReturn(5L);
        when(opportunityRepo.countByEtapaOportunidad(StageOpportunity.CALIFICACION)).thenReturn(5L);
        when(opportunityRepo.countByEtapaOportunidad(StageOpportunity.PROPUESTA)).thenReturn(10L);
        when(opportunityRepo.countByEtapaOportunidad(StageOpportunity.NEGOCIACION)).thenReturn(5L);
        when(opportunityRepo.countByEtapaOportunidad(StageOpportunity.CIERRE_GANADO)).thenReturn(40L);
        when(opportunityRepo.countByEtapaOportunidad(StageOpportunity.CIERRE_PERDIDO)).thenReturn(10L);

        // Act
        GerenteDashboardDTO dto = dashboardService.getGerenteDashboard();

        // Assert
        assertNotNull(dto);
        assertEquals(100L, dto.totalClientes());
        assertEquals(60L, dto.totalVentas());
        assertEquals(25L, dto.oportunidadesActivas());
        assertEquals(40L, dto.oportunidadesGanadas());
        assertEquals(10L, dto.oportunidadesPerdidas());
        assertEquals(BigDecimal.valueOf(300000.0), dto.valorOportunidadesGanadas());

        // Embudo
        assertEquals(5L, dto.enProspeccion());
        assertEquals(5L, dto.enCalificacion());
        assertEquals(10L, dto.enPropuesta());
        assertEquals(5L, dto.enNegociacion());
        assertEquals(40L, dto.enCierreGanado());
        assertEquals(10L, dto.enCierrePerdido());
    }

    @Test
    @DisplayName("Debe retornar el Dashboard del Asesor con sus métricas y embudo personal")
    void debeRetornarAsesorDashboard() {
        // Arrange
        Long asesorId = 2L;

        when(opportunityRepo.countByAsesorId(asesorId)).thenReturn(12L);
        when(opportunityRepo.countByAsesorIdAndEstado(asesorId, StatusOpportunity.ACTIVA)).thenReturn(6L);
        when(opportunityRepo.countByAsesorIdAndEstado(asesorId, StatusOpportunity.GANADA)).thenReturn(4L);
        when(opportunityRepo.countByAsesorIdAndEstado(asesorId, StatusOpportunity.PERDIDA)).thenReturn(2L);
        when(opportunityRepo.sumValorEstimadoByAsesorIdAndEstado(asesorId, StatusOpportunity.GANADA)).thenReturn(BigDecimal.valueOf(45000.0));
        when(alertaRepo.countByUsuarioIdAndEstado(asesorId, StateAlert.PENDIENTE)).thenReturn(3L);

        when(opportunityRepo.countByAsesorIdAndEtapaOportunidad(asesorId, StageOpportunity.PROSPECCION)).thenReturn(2L);
        when(opportunityRepo.countByAsesorIdAndEtapaOportunidad(asesorId, StageOpportunity.CALIFICACION)).thenReturn(1L);
        when(opportunityRepo.countByAsesorIdAndEtapaOportunidad(asesorId, StageOpportunity.PROPUESTA)).thenReturn(2L);
        when(opportunityRepo.countByAsesorIdAndEtapaOportunidad(asesorId, StageOpportunity.NEGOCIACION)).thenReturn(1L);
        when(opportunityRepo.countByAsesorIdAndEtapaOportunidad(asesorId, StageOpportunity.CIERRE_GANADO)).thenReturn(4L);
        when(opportunityRepo.countByAsesorIdAndEtapaOportunidad(asesorId, StageOpportunity.CIERRE_PERDIDO)).thenReturn(2L);

        // Act
        AsesorDashboardDTO dto = dashboardService.getAsesorDashboard(asesorId);

        // Assert
        assertNotNull(dto);
        assertEquals(12L, dto.misOportunidades());
        assertEquals(6L, dto.misOportunidadesActivas());
        assertEquals(4L, dto.misOportunidadesGanadas());
        assertEquals(2L, dto.misOportunidadesPerdidas());
        assertEquals(BigDecimal.valueOf(45000.0), dto.miValorGanado());
        assertEquals(3L, dto.misAlertasPendientes());

        // Embudo personal
        assertEquals(2L, dto.enProspeccion());
        assertEquals(1L, dto.enCalificacion());
        assertEquals(2L, dto.enPropuesta());
        assertEquals(1L, dto.enNegociacion());
        assertEquals(4L, dto.enCierreGanado());
        assertEquals(2L, dto.enCierrePerdido());
    }
}