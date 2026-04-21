package com.terreneitors.backendclintec.oportunidades.infrastructure.web;

import com.terreneitors.backendclintec.oportunidades.application.service.OportunidadCrudService;
import com.terreneitors.backendclintec.oportunidades.domain.EtapaOportunidad;
import com.terreneitors.backendclintec.oportunidades.domain.Oportunidad;
import com.terreneitors.backendclintec.oportunidades.infrastructure.dto.OportunidadRequestDTO;
import com.terreneitors.backendclintec.oportunidades.infrastructure.dto.OportunidadResponseDTO;
import com.terreneitors.backendclintec.oportunidades.infrastructure.persistence.mapper.OportunidadPersistenceMapper;
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
public class OportunidadesController {
    private final OportunidadCrudService oportunidadesCrudService;
    private final OportunidadPersistenceMapper mapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR','GERENTE')")
    public ResponseEntity<List<OportunidadResponseDTO>> listar(){
        List<OportunidadResponseDTO> oporunidades = oportunidadesCrudService.findAll().stream().map(mapper::toDTO).toList();
        return ResponseEntity.ok(oporunidades);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR','GERENTE')")
    public ResponseEntity<OportunidadResponseDTO> buscarPorId(@PathVariable Long id){
        return oportunidadesCrudService.buscarPorId(id)
                .map(u-> mapper.toDTO(u))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/idasesor/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR','GERENTE')")
    public ResponseEntity<List<OportunidadResponseDTO>> buscarPorIdAsesor(@PathVariable Long id){
        return ResponseEntity.ok(oportunidadesCrudService.buscarPorIdAsesor(id).stream().map(mapper::toDTO).toList());
    }
    @GetMapping("/idcliente/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR','GERENTE')")
    public ResponseEntity<List<OportunidadResponseDTO>> buscarPorIdCliente(@PathVariable Long id){
        return ResponseEntity.ok(oportunidadesCrudService.buscarPorIdCliente(id).stream().map(mapper::toDTO).toList());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR')")
    public ResponseEntity<OportunidadResponseDTO> crear (@Valid @RequestBody OportunidadRequestDTO dto){
        Oportunidad oportunidad = oportunidadesCrudService.crearOportunidades(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(oportunidad));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR','GERENTE')")
    public ResponseEntity<OportunidadResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody OportunidadRequestDTO dto){
        Oportunidad actualizado = oportunidadesCrudService.actulizarOportunidades(id, dto);
        return ResponseEntity.ok(mapper.toDTO(actualizado));
    }

    @PatchMapping("/{id}/etapa")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ASESOR')")
    public ResponseEntity<OportunidadResponseDTO> cambiarEtapa(
            @PathVariable Long id, @RequestParam EtapaOportunidad etapa) {
        return ResponseEntity.ok(mapper.toDTO(oportunidadesCrudService.cambiarEtapa(id, etapa)));
    }

    @PatchMapping("/{id}/probabilidad")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ASESOR')")
    public ResponseEntity<OportunidadResponseDTO> ajustarProbabilidad(
            @PathVariable Long id, @RequestParam int probabilidad) {
        return ResponseEntity.ok(mapper.toDTO(oportunidadesCrudService.ajustarProbabilidad(id, probabilidad)));
    }

    @PatchMapping("/{id}/ganar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE')")
    public ResponseEntity<Void> cerrarGanada(@PathVariable Long id) {
        oportunidadesCrudService.cerrarComoGanada(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/perder")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE')")
    public ResponseEntity<Void> cerrarPerdida(@PathVariable Long id) {
        oportunidadesCrudService.cerrarComoPerdida(id);
        return ResponseEntity.noContent().build();
    }

}
