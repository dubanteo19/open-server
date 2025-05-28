package com.nonglam.open_server.domain.chat.message.dto;

public record MessageCreateRequest(
    String content,
    Long receiverId
) {
}
