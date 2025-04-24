package com.nonglam.open_server.domain.comment.dto.requset;

public record CommentUpdateRequest(
        CommentPayload payload,
        Long commentId
) {
}
