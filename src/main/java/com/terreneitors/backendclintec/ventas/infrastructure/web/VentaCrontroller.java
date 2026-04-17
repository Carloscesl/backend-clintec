package com.terreneitors.backendclintec.ventas.infrastructure.web;

import com.terreneitors.backendclintec.ventas.application.port.in.ventasCrudUseCase;
import com.terreneitors.backendclintec.ventas.infrastructure.dto.VentaRequestDTO;
import com.terreneitors.backendclintec.ventas.infrastructure.dto.VentaResponseDTO;
import com.terreneitors.backendclintec.ventas.infrastructure.persistence.mapper.VentaPersistenceMapper;
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
public class VentaCrontroller {
    private final ventasCrudUseCase ventaCasosUso;
    private final VentaPersistenceMapper ventaMapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR','GERENTE')")
    public ResponseEntity<List<VentaResponseDTO>> listarTodos(){
        return ResponseEntity.ok(ventaCasosUso.findAll().stream().map(ventaMapper::toDTO).toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR','GERENTE')")
    public ResponseEntity<VentaResponseDTO> buscarId(@PathVariable Long id){
        return ventaCasosUso.findId(id)
                .map(ventaMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscarasesor/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR','GERENTE')")
    public ResponseEntity<VentaResponseDTO> buscarAsesor(@PathVariable Long id){
        return ventaCasosUso.findIdAsesor(id)
                .map(ventaMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscaroportunidad/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR','GERENTE')")
    public ResponseEntity<VentaResponseDTO> buscarOportunidad(@PathVariable Long id){
        return ventaCasosUso.findIdOportunidad(id)
                .map(ventaMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR','GERENTE','ASESOR')")
    public ResponseEntity<VentaResponseDTO> crearVenta(@Valid @RequestBody VentaRequestDTO ventaDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaMapper.toDTO(ventaCasosUso.createVenta(ventaDTO)));
    }

    @PutMapping("/actualizarventa/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR','GERENTE')")
    public ResponseEntity<VentaResponseDTO> actualizarVenta(@PathVariable Long id, @Valid @RequestBody VentaRequestDTO ventaDTO){
        return ResponseEntity.ok(ventaMapper.toDTO(ventaCasosUso.updateVenta(id, ventaDTO)));
    }
}
