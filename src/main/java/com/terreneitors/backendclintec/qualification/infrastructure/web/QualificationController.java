package com.terreneitors.backendclintec.qualification.infrastructure.web;

import com.terreneitors.backendclintec.qualification.application.port.in.QualificationCrudUseCase;
import com.terreneitors.backendclintec.qualification.infrastructure.dto.QualificationRequestDTO;
import com.terreneitors.backendclintec.qualification.infrastructure.dto.QualificationResponseDTO;
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
}
