package com.nonglam.open_server.domain.chat.conversation;

import java.util.List;

import org.springframework.stereotype.Component;

import com.nonglam.open_server.domain.chat.conversation.dto.ConversationResponse;
import com.nonglam.open_server.domain.chat.conversation.dto.ConversationSummaryResponse;
import com.nonglam.open_server.domain.chat.message.Message;
import com.nonglam.open_server.domain.chat.message.MessageMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConversationMapper {
  private final MessageMapper messageMapper;

  public List<ConversationSummaryResponse> toSummaryList(List<Conversation> conversations, Long currentOpenerId) {
    return conversations
        .stream()
        .map(c -> toSummary(c, currentOpenerId))
        .toList();
  }

  public ConversationSummaryResponse toSummary(Conversation conversation, Long currentOpenerId) {
    var opener1 = conversation.getOpener1();
    var opener2 = conversation.getOpener2();
    String name = opener1.getId() == currentOpenerId ? opener2.getUsername() : opener1.getUsername();
    String avatar = opener1.getId() == currentOpenerId ? opener2.getAvatarUrl() : opener1.getAvatarUrl();
    String receiver = opener1.getId() == currentOpenerId ? opener2.getUsername() : opener1.getUsername();
    return new ConversationSummaryResponse(
        conversation.getId(),
        name,
        avatar,
        receiver);
  }

  public ConversationResponse toResponse(Conversation conversation, List<Message> messages, Long currentOpenerId) {
    var summary = toSummary(conversation, currentOpenerId);
    var messageResponseList = messageMapper.toResponseList(messages);
    return new ConversationResponse(summary, messageResponseList);
  }
}
