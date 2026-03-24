package com.terreneitors.backendclintec.auth.application.port.in;

import com.terreneitors.backendclintec.auth.domain.AuthUsuario;
import com.terreneitors.backendclintec.auth.infrastructure.dto.TokenResponse;

public interface LoginUseCase {
    TokenResponse login(String email, String password);
}
