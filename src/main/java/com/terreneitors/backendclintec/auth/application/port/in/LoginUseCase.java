package com.terreneitors.backendclintec.auth.application.port.in;

import com.terreneitors.backendclintec.auth.domain.AuthUsuario;

public interface LoginUseCase {
    String login(String email, String password);
}
