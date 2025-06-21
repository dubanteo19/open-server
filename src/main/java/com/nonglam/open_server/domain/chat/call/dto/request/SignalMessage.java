package com.nonglam.open_server.domain.chat.call.dto.request;

public record SignalMessage(
    SinalType sinalType,
    String sdp,
    String from,
    String to,
    Object candidate) {
  enum SinalType {
    OFFER, ANSWER, ICE
  }
}
