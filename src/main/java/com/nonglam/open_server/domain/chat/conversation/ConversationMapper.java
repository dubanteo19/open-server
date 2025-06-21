package com.nonglam.open_server.domain.chat.conversation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

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
    var lastMessage = conversation.getLastMessage();
    String lastMessageContent = null;
    LocalDateTime lastMessageSentAt = null;
    if (lastMessage != null) {
      lastMessageContent = lastMessage.getContent();
      lastMessageSentAt = lastMessage.getCreatedAt();
    }
    String name = opener1.getId() == currentOpenerId ? opener2.getUsername() : opener1.getUsername();
    String avatar = opener1.getId() == currentOpenerId ? opener2.getAvatarUrl() : opener1.getAvatarUrl();
    String receiver = opener1.getId() == currentOpenerId ? opener2.getUsername() : opener1.getUsername();
    long unseenCount = conversation.getMessages()
        .stream()
        .filter(message -> message.getSender().getId() != currentOpenerId)
        .filter(Predicate.not(Message::isSeen)).count();
    return new ConversationSummaryResponse(
        conversation.getId(),
        name,
        avatar,
        receiver,
        unseenCount,
        lastMessageContent,
        lastMessageSentAt);
  }

  public ConversationResponse toResponse(Conversation conversation, List<Message> messages, Long currentOpenerId) {
    var summary = toSummary(conversation, currentOpenerId);
    var messageResponseList = messageMapper.toResponseList(messages);
    return new ConversationResponse(summary, messageResponseList);
  }
}
