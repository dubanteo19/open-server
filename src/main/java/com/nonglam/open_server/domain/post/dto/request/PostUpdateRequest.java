package com.nonglam.open_server.domain.post.dto.request;

public record PostUpdateRequest(
        PostPayload payload,
        Long postId
) {
}
