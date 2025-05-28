package com.nonglam.open_server.domain.chat.conversation.dto;

public record ConversationResponse(
    Long id,
    String name,
    String avatar,
    Long receiverId) {
}
