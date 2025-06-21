package com.nonglam.open_server.domain.chat.conversation.dto;

import java.time.LocalDateTime;

public record ConversationSummaryResponse(
    Long id,
    String name,
    String avatar,
    String receiver,
    long unseenCount,
    String lastMessageContent,
    LocalDateTime lastMessageSentAt) {
}
