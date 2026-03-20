package com.terreneitors.backendclintec.auth.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenResponse(
        String token
) {
}
