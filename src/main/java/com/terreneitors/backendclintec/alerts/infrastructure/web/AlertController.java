package com.terreneitors.backendclintec.alerts.infrastructure.web;

import com.terreneitors.backendclintec.alerts.application.port.in.AlertCrudUseCase;
import com.terreneitors.backendclintec.alerts.infrastructure.dto.AlertResponseDTO;
import com.terreneitors.backendclintec.alerts.infrastructure.persistence.Mapper.AlertPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alertas")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class AlertController {
    private final AlertCrudUseCase useCase;
    private final AlertPersistenceMapper mapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE')")
    public ResponseEntity<List<AlertResponseDTO>> listar() {
        return ResponseEntity.ok(
                useCase.findAll().stream().map(mapper::toDTO).toList());
    }

    @GetMapping("/pendientes")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','ASESOR')")
    public ResponseEntity<List<AlertResponseDTO>> pendientes() {
        return ResponseEntity.ok(
                useCase.buscarPendientes().stream().map(mapper::toDTO).toList());
    }

    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','ASESOR')")
    public ResponseEntity<List<AlertResponseDTO>> porCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(
                useCase.buscarPorCliente(clienteId).stream().map(mapper::toDTO).toList());
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE')")
    public ResponseEntity<List<AlertResponseDTO>> porUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(
                useCase.buscarPorUsuario(usuarioId).stream().map(mapper::toDTO).toList());
    }

    @PatchMapping("/{id}/vista")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','ASESOR')")
    public ResponseEntity<AlertResponseDTO> marcarVista(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDTO(useCase.marcarComoVista(id)));
    }

    @PatchMapping("/{id}/resolver")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','ASESOR')")
    public ResponseEntity<AlertResponseDTO> resolver(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDTO(useCase.resolver(id)));
    }
}
