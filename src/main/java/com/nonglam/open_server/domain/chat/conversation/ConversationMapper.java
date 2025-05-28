package com.nonglam.open_server.domain.chat.conversation;

import java.util.List;

import org.springframework.stereotype.Component;

import com.nonglam.open_server.domain.chat.conversation.dto.ConversationResponse;

@Component
public class ConversationMapper {
  public List<ConversationResponse> toResponseList(List<Conversation> conversations, Long currentOpenerId) {
    return conversations
        .stream()
        .map(c -> toResponse(c, currentOpenerId))
        .toList();
  }

  public ConversationResponse toResponse(Conversation conversation, Long currentOpenerId) {
    var opener1 = conversation.getOpener1();
    var opener2 = conversation.getOpener2();
    String name = opener1.getId() == currentOpenerId ? opener2.getUsername() : opener1.getUsername();
    String avatar = opener1.getId() == currentOpenerId ? opener2.getAvatarUrl() : opener1.getAvatarUrl();
    Long receiverId = opener1.getId() == currentOpenerId ? opener2.getId() : opener1.getId();
    return new ConversationResponse(
        conversation.getId(),
        name,
        avatar,
        receiverId
    );
  }
}
