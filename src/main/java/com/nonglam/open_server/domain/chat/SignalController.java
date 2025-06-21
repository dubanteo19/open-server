package com.nonglam.open_server.domain.chat;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.nonglam.open_server.domain.chat.call.dto.request.SignalMessage;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SignalController {
  private final SimpMessagingTemplate messagingTemplate;

  @MessageMapping("/call.offer")
  public void offer(SignalMessage signalMessage) {
    String topic = "/queue/call.offer";
    messagingTemplate.convertAndSendToUser(signalMessage.to(), topic, signalMessage);
  }

  @MessageMapping("/call.answer")
  public void answer(Principal principal, SignalMessage signalMessage) {
    String topic = "/queue/call.answer";
    messagingTemplate.convertAndSendToUser(signalMessage.to(), topic, signalMessage);
  }

  @MessageMapping("/call.ice")
  public void ice(Principal principal, SignalMessage signalMessage) {
    String topic = "/queue/call.ice";
    System.out.println(signalMessage);
    messagingTemplate.convertAndSendToUser(signalMessage.to(), topic, signalMessage);
  }
}
