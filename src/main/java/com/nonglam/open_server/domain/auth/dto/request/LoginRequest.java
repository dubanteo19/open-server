package com.nonglam.open_server.domain.auth.dto.request;

public record LoginRequest(
        String email,
        String password
) {
}
