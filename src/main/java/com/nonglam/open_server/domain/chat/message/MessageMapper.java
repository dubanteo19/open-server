package com.nonglam.open_server.domain.chat.message;

import java.util.List;

import org.springframework.stereotype.Component;

import com.nonglam.open_server.domain.chat.message.dto.MessageResponse;
import com.nonglam.open_server.domain.user.OpenerMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MessageMapper {
  private final OpenerMapper openerMapper;

  public List<MessageResponse> toResponseList(List<Message> messageList) {
    return messageList.stream().map(this::toResponse).toList();
  }

  public MessageResponse toResponse(Message message) {
    var sender = openerMapper.toOpenerResponse(message.getSender());
    return new MessageResponse(
        message.getId(),
        message.getConversation().getId(),
        message.getContent(),
        sender,
        false,
        message.getCreatedAt());
  }
}
