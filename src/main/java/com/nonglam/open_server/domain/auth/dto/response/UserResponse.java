package com.nonglam.open_server.domain.auth.dto.response;

public record UserResponse(
        Long id,
        String username,
        String displayName,
        String avatarUrl
) {
}
