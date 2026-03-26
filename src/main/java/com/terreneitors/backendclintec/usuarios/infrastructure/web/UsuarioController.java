package com.terreneitors.backendclintec.usuarios.infrastructure.web;

import com.terreneitors.backendclintec.usuarios.application.service.UserCrudService;
import com.terreneitors.backendclintec.usuarios.domain.Usuario;
import com.terreneitors.backendclintec.usuarios.infrastructure.dto.UsuarioRequestDTO;
import com.terreneitors.backendclintec.usuarios.infrastructure.dto.UsuarioResponseDTO;
import com.terreneitors.backendclintec.usuarios.infrastructure.persistence.mapper.UsuarioPersistenceMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class UsuarioController {

    private final UserCrudService userCrudService;
    private final UsuarioPersistenceMapper mapper;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar(){
        List<UsuarioResponseDTO> usuarios = userCrudService.findAll().stream().map(mapper::toDTO).toList();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        return userCrudService.buscarPorId(id)
                .map(u -> mapper.toDTO(u))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(@Valid @RequestBody UsuarioRequestDTO dto) {
        Usuario creado = userCrudService.crearUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDTO dto) {
        Usuario actualizado = userCrudService.actualizarUsuario(id, dto);
        return ResponseEntity.ok(mapper.toDTO(actualizado));
    }

    @PatchMapping("/{email}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable String email) {
        userCrudService.desactivarUsuario(email);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{email}/activar")
    public ResponseEntity<Void> activar(@PathVariable String email) {
        userCrudService.activarUsuario(email);
        return ResponseEntity.noContent().build();
    }


}
