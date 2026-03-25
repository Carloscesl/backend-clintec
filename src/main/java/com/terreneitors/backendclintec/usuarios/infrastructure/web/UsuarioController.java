package com.terreneitors.backendclintec.usuarios.infrastructure.web;

import com.terreneitors.backendclintec.usuarios.application.services.UserCrudService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin("http://localhost:4200")
public class UsuarioController {
    private final UserCrudService userCrudService;

    public UsuarioController(UserCrudService userCrudService) {
        this.userCrudService = userCrudService;
    }
    @GetMapping
    public ResponseEntity<List> listar(){
        return ResponseEntity.ok(userCrudService.findAll());
    }



}
