package com.nonglam.open_server.domain.post.dto.request;

public record PostCreateRequest(
        Long openerId,
        PostPayload payload
) {
}
