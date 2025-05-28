package com.nonglam.open_server.domain.chat.message.dto;

import java.time.LocalDateTime;

import com.nonglam.open_server.domain.user.dto.response.OpenerResponse;

public record MessageResponse(
    Long id,
    String content,
    OpenerResponse sender,
    boolean seen,
    LocalDateTime createdAt) {
}
