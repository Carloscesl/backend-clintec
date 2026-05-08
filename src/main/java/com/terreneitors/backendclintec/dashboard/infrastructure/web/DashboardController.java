package com.terreneitors.backendclintec.dashboard.infrastructure.web;

import com.terreneitors.backendclintec.dashboard.application.DashboardService;
import com.terreneitors.backendclintec.dashboard.infrastructure.dto.AdminDashboardDTO;
import com.terreneitors.backendclintec.dashboard.infrastructure.dto.AsesorDashboardDTO;
import com.terreneitors.backendclintec.dashboard.infrastructure.dto.GerenteDashboardDTO;
import com.terreneitors.backendclintec.users.application.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final UserRepositoryPort usuarioRepo;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<AdminDashboardDTO> getAdmin() {
        return ResponseEntity.ok(dashboardService.getAdminDashboard());
    }

    @GetMapping("/gerente")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    public ResponseEntity<GerenteDashboardDTO> getGerente() {
        return ResponseEntity.ok(dashboardService.getGerenteDashboard());
    }

    @GetMapping("/asesor")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR')")
    public ResponseEntity<AsesorDashboardDTO> getAsesor(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long asesorId = usuarioRepo
                .findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getId();
        return ResponseEntity.ok(
                dashboardService.getAsesorDashboard(asesorId));
    }
}