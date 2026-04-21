package com.terreneitors.backendclintec.users.infrastructure.web;

import com.terreneitors.backendclintec.users.application.service.UserCrudService;
import com.terreneitors.backendclintec.users.domain.User;
import com.terreneitors.backendclintec.users.infrastructure.dto.UserRequestDTO;
import com.terreneitors.backendclintec.users.infrastructure.dto.UserResponseDTO;
import com.terreneitors.backendclintec.users.infrastructure.dto.UserUpdateDTO;
import com.terreneitors.backendclintec.users.infrastructure.persistence.mapper.UserPersistenceMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class UserController {

    private final UserCrudService userCrudService;
    private final UserPersistenceMapper mapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<UserResponseDTO>> list(){
        List<UserResponseDTO> usuarios = userCrudService.findAll().stream().map(mapper::toDTO).toList();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        return userCrudService.findById(id)
                .map(u -> mapper.toDTO(u))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UserResponseDTO> findByEmail(@PathVariable String email) {
        return userCrudService.findByEmail(email)
                .map(u -> mapper.toDTO(u))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserRequestDTO dto) {
        User creado = userCrudService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(creado));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO dto) {
        User actualizado = userCrudService.updateUser(id, dto);
        return ResponseEntity.ok(mapper.toDTO(actualizado));
    }

    @PatchMapping("/{email}/desactivar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> desactivate(@PathVariable String email) {
        userCrudService.desactivateUser(email);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{email}/activar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> activate(@PathVariable String email) {
        userCrudService.activateUser(email);
        return ResponseEntity.noContent().build();
    }


}
