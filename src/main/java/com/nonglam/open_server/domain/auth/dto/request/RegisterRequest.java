package com.nonglam.open_server.domain.auth.dto.request;

public record RegisterRequest(
        String email,
        String username,
        String displayName,
        String password
) {
}
