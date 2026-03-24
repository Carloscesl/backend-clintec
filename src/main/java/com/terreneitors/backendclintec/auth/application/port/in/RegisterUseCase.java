package com.terreneitors.backendclintec.auth.application.port.in;

import com.terreneitors.backendclintec.auth.domain.AuthUsuario;
import com.terreneitors.backendclintec.auth.infrastructure.dto.TokenResponse;

public interface RegisterUseCase {
    TokenResponse register(String nombreUser, String email, String password);
}
