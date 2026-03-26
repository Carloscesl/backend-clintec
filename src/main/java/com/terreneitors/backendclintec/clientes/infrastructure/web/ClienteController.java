package com.terreneitors.backendclintec.clientes.infrastructure.web;

import com.terreneitors.backendclintec.clientes.application.service.ClienteCrudService;
import com.terreneitors.backendclintec.clientes.domain.Cliente;
import com.terreneitors.backendclintec.clientes.infrastructure.dto.ClienteRequestDTO;
import com.terreneitors.backendclintec.clientes.infrastructure.dto.ClienteResponseDTO;
import com.terreneitors.backendclintec.clientes.infrastructure.persistence.mapper.ClientePersistenceMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class ClienteController {
    private final ClienteCrudService clienteCrudService;
    private final ClientePersistenceMapper mapper;

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listar(){
        List<ClienteResponseDTO> clientes = clienteCrudService.findAll().stream().map(mapper::toDTO).toList();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id){
        return clienteCrudService.buscarPorId(id)
                .map(u-> mapper.toDTO(u))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<ClienteResponseDTO> buscarPorEmail(@PathVariable String email){
        return clienteCrudService.buscarPorEmail(email)
                .map(u-> mapper.toDTO(u))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crear(@Valid @RequestBody ClienteRequestDTO dto){
        Cliente creado = clienteCrudService.crearCliente(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO dto){
        Cliente actualizado = clienteCrudService.actualizarCliente(id, dto);
        return ResponseEntity.ok(mapper.toDTO(actualizado));
    }


}
