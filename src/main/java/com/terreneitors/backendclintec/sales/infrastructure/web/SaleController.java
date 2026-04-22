package com.terreneitors.backendclintec.sales.infrastructure.web;

import com.terreneitors.backendclintec.sales.application.port.in.SaleCrudUseCase;
import com.terreneitors.backendclintec.sales.infrastructure.dto.SaleRequestDTO;
import com.terreneitors.backendclintec.sales.infrastructure.dto.SaleResponseDTO;
import com.terreneitors.backendclintec.sales.infrastructure.persistence.Mapper.SalePersistenceMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venta")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class SaleController {

    private final SaleCrudUseCase ventaCasosUso;
    private final SalePersistenceMapper ventaMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE')")
    public ResponseEntity<List<SaleResponseDTO>> listAll(){
        return ResponseEntity.ok(ventaCasosUso.findAll().stream().map(ventaMapper::toDTO).toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE')")
    public ResponseEntity<SaleResponseDTO> findId(@PathVariable Long id){
        return ventaCasosUso.findId(id)
                .map(ventaMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscarasesor/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE')")
    public ResponseEntity<List<SaleResponseDTO>> findAssessor(@PathVariable Long id){
        return ResponseEntity.ok(ventaCasosUso.findIdAssessor(id).stream().map(ventaMapper::toDTO).toList());
    }

    @GetMapping("/buscaroportunidad/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE')")
    public ResponseEntity<List<SaleResponseDTO>> findOpportunity(@PathVariable Long id){
        return ResponseEntity.ok(ventaCasosUso.findIdOpportunity(id).stream().map(ventaMapper::toDTO).toList());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE','ASESOR')")
    public ResponseEntity<SaleResponseDTO> createSale(@Valid @RequestBody SaleRequestDTO ventaDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaMapper.toDTO(ventaCasosUso.createSale(ventaDTO)));
    }

    @PutMapping("/actualizarventa/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','GERENTE')")
    public ResponseEntity<SaleResponseDTO> updateSale(@PathVariable Long id, @Valid @RequestBody SaleRequestDTO ventaDTO){
        return ResponseEntity.ok(ventaMapper.toDTO(ventaCasosUso.updateSale(id, ventaDTO)));
    }
}
