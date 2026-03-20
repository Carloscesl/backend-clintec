package com.terreneitors.backendclintec.auth.application.port.in;

import com.terreneitors.backendclintec.auth.domain.AuthUsuario;

public interface RegisterUseCase {
    String register(String nombreUser, String email, String password);
}
