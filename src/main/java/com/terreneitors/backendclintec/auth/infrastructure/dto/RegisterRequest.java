package com.terreneitors.backendclintec.auth.infrastructure.dto;

public record RegisterRequest(
        String nombreUser,
        String email,
        String password
) {
}
