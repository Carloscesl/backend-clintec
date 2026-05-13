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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final ClientRepositoryPort clienteRepo;
    private final UserRepositoryPort usuarioRepo;
    private final OpportunityRepositoryPort opportunityRepo;
    private final SaleRepositoryPort ventaRepo;
    private final AlertRepositoryPort alertaRepo;

    public AdminDashboardDTO getAdminDashboard() {
        return new AdminDashboardDTO(
                clienteRepo.count(),
                usuarioRepo.count(),
                ventaRepo.count(),
                alertaRepo.countByEstado(StateAlert.PENDIENTE),
                opportunityRepo.countByEstado(StatusOpportunity.ACTIVA),
                opportunityRepo.countByEstado(StatusOpportunity.GANADA),
                opportunityRepo.countByEstado(StatusOpportunity.PERDIDA),
                opportunityRepo.sumValorEstimadoByEstado(StatusOpportunity.GANADA),
                opportunityRepo.countByEtapaOportunidad(StageOpportunity.PROSPECCION),
                opportunityRepo.countByEtapaOportunidad(StageOpportunity.CALIFICACION),
                opportunityRepo.countByEtapaOportunidad(StageOpportunity.PROPUESTA),
                opportunityRepo.countByEtapaOportunidad(StageOpportunity.NEGOCIACION),
                opportunityRepo.countByEtapaOportunidad(StageOpportunity.CIERRE_GANADO),
                opportunityRepo.countByEtapaOportunidad(StageOpportunity.CIERRE_PERDIDO)
        );
    }

    public GerenteDashboardDTO getGerenteDashboard() {
        return new GerenteDashboardDTO(
                clienteRepo.count(),
                ventaRepo.count(),
                opportunityRepo.countByEstado(StatusOpportunity.ACTIVA),
                opportunityRepo.countByEstado(StatusOpportunity.GANADA),
                opportunityRepo.countByEstado(StatusOpportunity.PERDIDA),
                opportunityRepo.sumValorEstimadoByEstado(StatusOpportunity.GANADA),
                opportunityRepo.countByEtapaOportunidad(StageOpportunity.PROSPECCION),
                opportunityRepo.countByEtapaOportunidad(StageOpportunity.CALIFICACION),
                opportunityRepo.countByEtapaOportunidad(StageOpportunity.PROPUESTA),
                opportunityRepo.countByEtapaOportunidad(StageOpportunity.NEGOCIACION),
                opportunityRepo.countByEtapaOportunidad(StageOpportunity.CIERRE_GANADO),
                opportunityRepo.countByEtapaOportunidad(StageOpportunity.CIERRE_PERDIDO)
        );
    }

    public AsesorDashboardDTO getAsesorDashboard(Long asesorId) {
        return new AsesorDashboardDTO(
                opportunityRepo.countByAsesorId(asesorId),
                opportunityRepo.countByAsesorIdAndEstado(asesorId, StatusOpportunity.ACTIVA),
                opportunityRepo.countByAsesorIdAndEstado(asesorId, StatusOpportunity.GANADA),
                opportunityRepo.countByAsesorIdAndEstado(asesorId, StatusOpportunity.PERDIDA),
                opportunityRepo.sumValorEstimadoByEstado(StatusOpportunity.GANADA),
                alertaRepo.countByUsuarioIdAndEstado(asesorId, StateAlert.PENDIENTE),
                opportunityRepo.countByAsesorIdAndEtapaOportunidad(asesorId, StageOpportunity.PROSPECCION),
                opportunityRepo.countByAsesorIdAndEtapaOportunidad(asesorId, StageOpportunity.CALIFICACION),
                opportunityRepo.countByAsesorIdAndEtapaOportunidad(asesorId, StageOpportunity.PROPUESTA),
                opportunityRepo.countByAsesorIdAndEtapaOportunidad(asesorId, StageOpportunity.NEGOCIACION),
                opportunityRepo.countByAsesorIdAndEtapaOportunidad(asesorId, StageOpportunity.CIERRE_GANADO),
                opportunityRepo.countByAsesorIdAndEtapaOportunidad(asesorId, StageOpportunity.CIERRE_PERDIDO)
        );
    }
}
