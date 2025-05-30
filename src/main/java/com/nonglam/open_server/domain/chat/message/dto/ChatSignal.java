package com.nonglam.open_server.domain.chat.message.dto;

public record ChatSignal(
    String to,
    String from,
    Type type,
    Long conversationId) {

  enum Type {
    TYPING, READ,
  }

}
