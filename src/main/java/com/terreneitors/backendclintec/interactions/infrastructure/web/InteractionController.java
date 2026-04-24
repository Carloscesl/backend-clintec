package com.terreneitors.backendclintec.interactions.infrastructure.web;

import com.terreneitors.backendclintec.interactions.application.port.in.InteractionCrudUseCase;
import com.terreneitors.backendclintec.interactions.infrastructure.dto.InteractionRequestDTO;
import com.terreneitors.backendclintec.interactions.infrastructure.dto.InteractionResponseDTO;
import com.terreneitors.backendclintec.interactions.infrastructure.persistence.Mapper.InteractionPersistenceMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interacciones")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class InteractionController {

    private final InteractionCrudUseCase useCase;
    private final InteractionPersistenceMapper mapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','ASESOR')")
    public ResponseEntity<List<InteractionResponseDTO>> listar() {
        return ResponseEntity.ok(
                useCase.findAll().stream().map(mapper::toDTO).toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','ASESOR')")
    public ResponseEntity<InteractionResponseDTO> buscarPorId(@PathVariable Long id) {
        return useCase.findById(id)
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','ASESOR')")
    public ResponseEntity<List<InteractionResponseDTO>> porCliente(
            @PathVariable Long clienteId) {
        return ResponseEntity.ok(
                useCase.findByIdClient(clienteId).stream().map(mapper::toDTO).toList());
    }

    @GetMapping("/oportunidad/{oportunidadId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','ASESOR')")
    public ResponseEntity<List<InteractionResponseDTO>> porOportunidad(
            @PathVariable Long oportunidadId) {
        return ResponseEntity.ok(
                useCase.findByIdOpportunities(oportunidadId).stream().map(mapper::toDTO).toList());
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE')")
    public ResponseEntity<List<InteractionResponseDTO>> porUsuario(
            @PathVariable Long usuarioId) {
        return ResponseEntity.ok(
                useCase.findByIdUser(usuarioId).stream().map(mapper::toDTO).toList());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ASESOR')")
    public ResponseEntity<InteractionResponseDTO> crear(
            @Valid @RequestBody InteractionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                mapper.toDTO(useCase.createInteraction(dto)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ASESOR')")
    public ResponseEntity<InteractionResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody InteractionRequestDTO dto) {
        return ResponseEntity.ok(
                mapper.toDTO(useCase.updateInteraction(id, dto)));
    }

}
