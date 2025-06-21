package com.nonglam.open_server.domain.chat.conversation.dto;

import java.util.List;

import com.nonglam.open_server.domain.chat.message.dto.MessageResponse;

public record ConversationResponse(
    ConversationSummaryResponse summary,
    List<MessageResponse> messages) {
}
