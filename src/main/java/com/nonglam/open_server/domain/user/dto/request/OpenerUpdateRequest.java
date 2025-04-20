package com.nonglam.open_server.domain.user.dto.request;

public record OpenerUpdateRequest(
        Long openerId,
        String displayName,
        String bio,
        String location
) {
}
