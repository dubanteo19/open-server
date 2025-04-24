package com.nonglam.open_server.domain.comment.event;

public record CommentCreatedEvent(
    Long postId,
    Long commentId,
    String username) {
}
