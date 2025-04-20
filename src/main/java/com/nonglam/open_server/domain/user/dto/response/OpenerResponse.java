package com.nonglam.open_server.domain.user.dto.response;

public record OpenerResponse(
        Long id,
        String username,
        String displayName,
        boolean verified,
        String avatarUrl
) {
}
