package com.terreneitors.backendclintec.opportunities.infrastructure.web;

import com.terreneitors.backendclintec.opportunities.application.service.OpportunityCrudService;
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
    private final OpportunityCrudService oportunidadesCrudService;
    private final OpportunityPersistenceMapper mapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR','GERENTE')")
    public ResponseEntity<List<OpportunityResponseDTO>> list(){
        List<OpportunityResponseDTO> oporunidades = oportunidadesCrudService.findAll().stream().map(mapper::toDTO).toList();
        return ResponseEntity.ok(oporunidades);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR','GERENTE')")
    public ResponseEntity<OpportunityResponseDTO> findById(@PathVariable Long id){
        return oportunidadesCrudService.findById(id)
                .map(u-> mapper.toDTO(u))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/idasesor/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR','GERENTE')")
    public ResponseEntity<List<OpportunityResponseDTO>> findByIdAssessor(@PathVariable Long id){
        return ResponseEntity.ok(oportunidadesCrudService.findByIdAssessor(id).stream().map(mapper::toDTO).toList());
    }
    @GetMapping("/idcliente/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR','GERENTE')")
    public ResponseEntity<List<OpportunityResponseDTO>> findByIdClient(@PathVariable Long id){
        return ResponseEntity.ok(oportunidadesCrudService.findByIdClient(id).stream().map(mapper::toDTO).toList());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR')")
    public ResponseEntity<OpportunityResponseDTO> create(@Valid @RequestBody OpportunityRequestDTO dto){
        Opportunity opportunity = oportunidadesCrudService.createOpportunities(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(opportunity));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR','GERENTE')")
    public ResponseEntity<OpportunityResponseDTO> update(@PathVariable Long id, @Valid @RequestBody OpportunityRequestDTO dto){
        Opportunity actualizado = oportunidadesCrudService.updateOpportunities(id, dto);
        return ResponseEntity.ok(mapper.toDTO(actualizado));
    }

    @PatchMapping("/{id}/etapa")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ASESOR')")
    public ResponseEntity<OpportunityResponseDTO> changeStage(
            @PathVariable Long id, @RequestParam StageOpportunity etapa) {
        return ResponseEntity.ok(mapper.toDTO(oportunidadesCrudService.changeStage(id, etapa)));
    }

    @PatchMapping("/{id}/probabilidad")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ASESOR')")
    public ResponseEntity<OpportunityResponseDTO> adjustProbability(
            @PathVariable Long id, @RequestParam int probabilidad) {
        return ResponseEntity.ok(mapper.toDTO(oportunidadesCrudService.adjustProbability(id, probabilidad)));
    }

    @PatchMapping("/{id}/ganar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE')")
    public ResponseEntity<Void> closeAsWon(@PathVariable Long id) {
        oportunidadesCrudService.closeAsWon(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/perder")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE')")
    public ResponseEntity<Void> closeAsLost(@PathVariable Long id) {
        oportunidadesCrudService.closeAsLost(id);
        return ResponseEntity.noContent().build();
    }

}
