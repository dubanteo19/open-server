package com.nonglam.open_server.domain.comment.event;

import com.nonglam.open_server.domain.comment.dto.response.CommentResponse;

public record CommentCreatedEvent(
    Long postId,
    CommentResponse comment,
    String username) {
}
