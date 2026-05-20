package com.terreneitors.backendclintec.opportunities.infrastructure.web;

import com.terreneitors.backendclintec.opportunities.application.port.in.OpportunityCrudUseCase;
import com.terreneitors.backendclintec.opportunities.domain.StageOpportunity;
import com.terreneitors.backendclintec.opportunities.domain.Opportunity;
import com.terreneitors.backendclintec.opportunities.infrastructure.dto.OpportunityRequestDTO;
import com.terreneitors.backendclintec.opportunities.infrastructure.dto.OpportunityResponseDTO;
import com.terreneitors.backendclintec.opportunities.infrastructure.persistence.mapper.OpportunityPersistenceMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/oportunidades")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class OpportunityController {
    private final OpportunityCrudUseCase oportunidadacaseUse;
    private final OpportunityPersistenceMapper mapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR','GERENTE')")
    public ResponseEntity<List<OpportunityResponseDTO>> list(){
        List<OpportunityResponseDTO> oporunidades = oportunidadacaseUse.findAll().stream().map(mapper::toDTO).toList();
        return ResponseEntity.ok(oporunidades);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR','GERENTE')")
    public ResponseEntity<OpportunityResponseDTO> findById(@PathVariable Long id){
        return oportunidadacaseUse.findById(id)
                .map(u-> mapper.toDTO(u))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/idasesor/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR','GERENTE')")
    public ResponseEntity<List<OpportunityResponseDTO>> findByIdAssessor(@PathVariable Long id){
        return ResponseEntity.ok(oportunidadacaseUse.findByIdAssessor(id).stream().map(mapper::toDTO).toList());
    }
    @GetMapping("/idcliente/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR','GERENTE')")
    public ResponseEntity<List<OpportunityResponseDTO>> findByIdClient(@PathVariable Long id){
        return ResponseEntity.ok(oportunidadacaseUse.findByIdClient(id).stream().map(mapper::toDTO).toList());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR')")
    public ResponseEntity<OpportunityResponseDTO> create(@Valid @RequestBody OpportunityRequestDTO dto){
        Opportunity opportunity = oportunidadacaseUse.createOpportunities(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(opportunity));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR','GERENTE')")
    public ResponseEntity<OpportunityResponseDTO> update(@PathVariable Long id, @Valid @RequestBody OpportunityRequestDTO dto){
        Opportunity actualizado = oportunidadacaseUse.updateOpportunities(id, dto);
        return ResponseEntity.ok(mapper.toDTO(actualizado));
    }

    @PatchMapping("/{id}/etapa")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ASESOR')")
    public ResponseEntity<OpportunityResponseDTO> changeStage(
            @PathVariable Long id, @RequestParam StageOpportunity etapa) {
        return ResponseEntity.ok(mapper.toDTO(oportunidadacaseUse.changeStage(id, etapa)));
    }

    @PatchMapping("/{id}/probabilidad")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ASESOR')")
    public ResponseEntity<OpportunityResponseDTO> adjustProbability(
            @PathVariable Long id, @RequestParam int probabilidad) {
        return ResponseEntity.ok(mapper.toDTO(oportunidadacaseUse.adjustProbability(id, probabilidad)));
    }

    @PatchMapping("/{id}/ganar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','ASESOR')")
    public ResponseEntity<Void> closeAsWon(@PathVariable Long id) {
        oportunidadacaseUse.closeAsWon(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/perder")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','ASESOR')")
    public ResponseEntity<Void> closeAsLost(@PathVariable Long id) {
        oportunidadacaseUse.closeAsLost(id);
        return ResponseEntity.noContent().build();
    }

}
