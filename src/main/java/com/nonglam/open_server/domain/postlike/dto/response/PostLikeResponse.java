package com.nonglam.open_server.domain.postlike.dto.response;

import java.time.LocalDateTime;

import com.nonglam.open_server.domain.user.dto.response.OpenerResponse;

public record PostLikeResponse(
    OpenerResponse likedBy,
    LocalDateTime likedAt) {
}
