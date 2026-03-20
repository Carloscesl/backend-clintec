package com.terreneitors.backendclintec.auth.infrastructure.web;

import com.terreneitors.backendclintec.auth.application.port.in.LoginUseCase;
import com.terreneitors.backendclintec.auth.application.port.in.RegisterUseCase;
import com.terreneitors.backendclintec.auth.infrastructure.dto.LoginRequest;
import com.terreneitors.backendclintec.auth.infrastructure.dto.RegisterRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RegisterUseCase registerUseCase;

    public AuthController(LoginUseCase loginUseCase, RegisterUseCase registerUseCase) {
        this.loginUseCase = loginUseCase;
        this.registerUseCase = registerUseCase;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        return loginUseCase.login(request.email(), request.password());
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return registerUseCase.register(
                request.nombreUser(),
                request.email(),
                request.password()
        );
    }
}
