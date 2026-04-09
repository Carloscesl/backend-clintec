package com.terreneitors.backendclintec.oportunidades.infrastructure.web;

import com.terreneitors.backendclintec.oportunidades.application.service.OportunidadesCrudService;
import com.terreneitors.backendclintec.oportunidades.domain.Oportunidades;
import com.terreneitors.backendclintec.oportunidades.infrastructure.dto.OportunidadesRequestDTO;
import com.terreneitors.backendclintec.oportunidades.infrastructure.dto.OportunidadesResponseDTO;
import com.terreneitors.backendclintec.oportunidades.infrastructure.persistence.mapper.OportunidadesPersistenceMapper;
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
    private final OportunidadesCrudService oportunidadesCrudService;
    private final OportunidadesPersistenceMapper mapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR','GERENTE')")
    public ResponseEntity<List<OportunidadesResponseDTO>> listar(){
        List<OportunidadesResponseDTO> oporunidades = oportunidadesCrudService.findAll().stream().map(mapper::toDTO).toList();
        return ResponseEntity.ok(oporunidades);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR','GERENTE')")
    public ResponseEntity<OportunidadesResponseDTO> buscarPorId(@PathVariable Long id){
        return oportunidadesCrudService.buscarPorId(id)
                .map(u-> mapper.toDTO(u))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/idasesor/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR','GERENTE')")
    public ResponseEntity<OportunidadesResponseDTO> buscarPorIdAsesor(@PathVariable Long id){
        return oportunidadesCrudService.buscarPorIdAsesor(id)
                .map(u-> mapper.toDTO(u))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/idcliente/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR','GERENTE')")
    public ResponseEntity<OportunidadesResponseDTO> buscarPorIdCliente(@PathVariable Long id){
        return oportunidadesCrudService.buscarPorIdCliente(id)
                .map(u-> mapper.toDTO(u))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR','GERENTE')")
    public ResponseEntity<OportunidadesResponseDTO> crear (@Valid @RequestBody OportunidadesRequestDTO dto){
        Oportunidades oportunidad = oportunidadesCrudService.crearOportunidades(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(oportunidad));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR','GERENTE')")
    public ResponseEntity<OportunidadesResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody OportunidadesRequestDTO dto){
        Oportunidades actualizado = oportunidadesCrudService.actulizarOportunidades(id, dto);
        return ResponseEntity.ok(mapper.toDTO(actualizado));
    }

}
