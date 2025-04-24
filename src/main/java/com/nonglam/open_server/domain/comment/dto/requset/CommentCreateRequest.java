package com.nonglam.open_server.domain.comment.dto.requset;

public record CommentCreateRequest(
        CommentPayload payload,
        Long authorId,
        Long postId
) {
}
