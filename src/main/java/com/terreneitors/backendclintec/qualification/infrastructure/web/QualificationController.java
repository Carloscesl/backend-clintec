package com.terreneitors.backendclintec.qualification.infrastructure.web;

import com.terreneitors.backendclintec.qualification.application.port.in.QualificationCrudUseCase;
import com.terreneitors.backendclintec.qualification.application.port.in.QualificationQueryUseCase;
import com.terreneitors.backendclintec.qualification.domain.Qualification;
import com.terreneitors.backendclintec.qualification.infrastructure.dto.QualificationDistribucionDTO;
import com.terreneitors.backendclintec.qualification.infrastructure.dto.QualificationHistoryResponseDTO;
import com.terreneitors.backendclintec.qualification.infrastructure.dto.QualificationRequestDTO;
import com.terreneitors.backendclintec.qualification.infrastructure.dto.QualificationResponseDTO;
import com.terreneitors.backendclintec.qualification.infrastructure.persistence.Mapper.QualificationHistoryPersistenceMapper;
import com.terreneitors.backendclintec.qualification.infrastructure.persistence.Mapper.QualificationPersistenceMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calificaciones")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class QualificationController {
    private final QualificationCrudUseCase useCase;
    private final QualificationPersistenceMapper mapper;
    private final QualificationQueryUseCase queryUseCase;
    private final QualificationHistoryPersistenceMapper historyMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE')")
    public ResponseEntity<List<QualificationResponseDTO>> listar() {
        return ResponseEntity.ok(
                useCase.findAll().stream().map(mapper::toDTO).toList());
    }

    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','ASESOR')")
    public ResponseEntity<QualificationResponseDTO> buscarPorCliente(
            @PathVariable Long clienteId) {
        return useCase.findByClientId(clienteId)
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/cliente/{clienteId}/puntaje")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE')")
    public ResponseEntity<QualificationResponseDTO> actualizarPuntaje(
            @PathVariable Long clienteId,
            @Valid @RequestBody QualificationRequestDTO dto) {
        return ResponseEntity.ok(
                mapper.toDTO(useCase.updateScore(clienteId, dto.puntaje())));
    }

    // GET /api/calificaciones/clasificacion/{nivel}
    @GetMapping("/clasificacion/{nivel}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE')")
    public ResponseEntity<List<QualificationResponseDTO>> porClasificacion(
            @PathVariable Qualification nivel) {
        return ResponseEntity.ok(
                queryUseCase.findByClasificacion(nivel)
                        .stream().map(mapper::toDTO).toList());
    }

    // GET /api/calificaciones/top/{n}
    @GetMapping("/top/{n}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE')")
    public ResponseEntity<List<QualificationResponseDTO>> topN(@PathVariable int n) {
        return ResponseEntity.ok(
                queryUseCase.findTopN(n)
                        .stream().map(mapper::toDTO).toList());
    }

    // GET /api/calificaciones/en-riesgo
    @GetMapping("/en-riesgo")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE')")
    public ResponseEntity<List<QualificationResponseDTO>> enRiesgo() {
        return ResponseEntity.ok(
                queryUseCase.findEnRiesgo()
                        .stream().map(mapper::toDTO).toList());
    }

    // GET /api/calificaciones/distribucion
    @GetMapping("/distribucion")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE')")
    public ResponseEntity<QualificationDistribucionDTO> distribucion() {
        return ResponseEntity.ok(
                new QualificationDistribucionDTO(queryUseCase.distribucionPorNivel()));
    }

    // GET /api/calificaciones/cliente/{clienteId}/historial
    @GetMapping("/cliente/{clienteId}/historial")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','ASESOR')")
    public ResponseEntity<List<QualificationHistoryResponseDTO>> historial(
            @PathVariable Long clienteId) {
        return ResponseEntity.ok(
                queryUseCase.historialPorCliente(clienteId)
                        .stream().map(historyMapper::toHistoryDTO).toList());
    }

}
