package com.nonglam.open_server.domain.comment.dto.response;

import com.nonglam.open_server.domain.user.dto.response.OpenerResponse;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        OpenerResponse author,
        LocalDateTime updatedAt
) {
}
