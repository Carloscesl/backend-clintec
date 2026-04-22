package com.terreneitors.backendclintec.clients.infrastructure.web;

import com.terreneitors.backendclintec.clients.application.service.ClientCrudService;
import com.terreneitors.backendclintec.clients.domain.Client;
import com.terreneitors.backendclintec.clients.infrastructure.dto.ClientRequestDTO;
import com.terreneitors.backendclintec.clients.infrastructure.dto.ClientResponseDTO;
import com.terreneitors.backendclintec.clients.infrastructure.persistence.mapper.ClientPersistenceMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class ClientController {
    private final ClientCrudService clienteCrudService;
    private final ClientPersistenceMapper mapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR','GERENTE')")
    public ResponseEntity<List<ClientResponseDTO>> list(){
        List<ClientResponseDTO> clientes = clienteCrudService.findAll().stream().map(mapper::toDTO).toList();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR','GERENTE')")
    public ResponseEntity<ClientResponseDTO> findById(@PathVariable Long id){
        return clienteCrudService.findById(id)
                .map(u-> mapper.toDTO(u))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR','GERENTE')")
    public ResponseEntity<ClientResponseDTO> findByEmail(@PathVariable String email){
        return clienteCrudService.findByEmail(email)
                .map(u-> mapper.toDTO(u))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR','GERENTE')")
    public ResponseEntity<ClientResponseDTO> create(@Valid @RequestBody ClientRequestDTO dto){
        Client creado = clienteCrudService.createClient(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(creado));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASESOR','GERENTE')")
    public ResponseEntity<ClientResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ClientRequestDTO dto){
        Client actualizado = clienteCrudService.updateClient(id, dto);
        return ResponseEntity.ok(mapper.toDTO(actualizado));
    }


}
