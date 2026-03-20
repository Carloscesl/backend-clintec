package com.terreneitors.backendclintec.auth.infrastructure.dto;

public record LoginRequest(
        String email,
        String password
) {
}
