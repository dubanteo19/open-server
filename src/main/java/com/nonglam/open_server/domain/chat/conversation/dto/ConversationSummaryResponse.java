package com.nonglam.open_server.domain.chat.conversation.dto;

public record ConversationSummaryResponse(
    Long id,
    String name,
    String avatar,
    String receiver) {
}
