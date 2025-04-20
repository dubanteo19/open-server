package com.nonglam.open_server.domain.post.dto.response;

import com.nonglam.open_server.domain.user.dto.response.OpenerResponse;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        OpenerResponse author,
        String content,
        int viewCount,
        int likeCount,
        int commentCount,
        LocalDateTime updatedAt
) {
}
