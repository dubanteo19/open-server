package com.nonglam.open_server.domain.auth.dto.response;

public record LoginResponse(
        UserResponse user,
        String accessToken,
        String refreshToken
) {
}
