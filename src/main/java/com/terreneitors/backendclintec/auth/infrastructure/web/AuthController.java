package com.terreneitors.backendclintec.auth.infrastructure.web;

import com.terreneitors.backendclintec.auth.application.port.in.LoginUseCase;
import com.terreneitors.backendclintec.auth.application.port.in.RegisterUseCase;
import com.terreneitors.backendclintec.auth.infrastructure.dto.LoginRequest;
import com.terreneitors.backendclintec.auth.infrastructure.dto.RegisterRequest;
import com.terreneitors.backendclintec.auth.infrastructure.dto.TokenResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class
AuthController {

    private final LoginUseCase loginUseCase;
    private final RegisterUseCase registerUseCase;

    public AuthController(LoginUseCase loginUseCase, RegisterUseCase registerUseCase) {
        this.loginUseCase = loginUseCase;
        this.registerUseCase = registerUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse response = loginUseCase.login(request.email(), request.password());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        TokenResponse response = registerUseCase.register(
                request.nombreUser(),
                request.email(),
                request.password()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED) ;
    }
}
