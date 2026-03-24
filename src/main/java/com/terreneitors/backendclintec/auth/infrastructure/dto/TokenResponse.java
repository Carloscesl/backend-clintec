package com.terreneitors.backendclintec.auth.infrastructure.dto;

import java.util.List;

public record TokenResponse(
        Long id,
        String username,
        String email,
        String token,
        List<String> roles
) {
}
