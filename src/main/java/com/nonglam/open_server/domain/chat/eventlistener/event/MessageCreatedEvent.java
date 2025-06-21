package com.nonglam.open_server.domain.chat.eventlistener.event;

public record MessageCreatedEvent(
    Long messageId,
    Long conversationId) {
}
