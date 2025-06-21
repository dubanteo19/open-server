package com.nonglam.open_server.domain.chat;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.nonglam.open_server.domain.chat.message.dto.ChatSignal;
import com.nonglam.open_server.domain.chat.message.dto.MessageCreateRequest;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RealTimeChatController {
  private final ChatService chatService;
  private final SimpMessagingTemplate messagingTemplate;

  @MessageMapping("/chat.typing")
  public void sendChatSinal(Principal principal, ChatSignal signal) {
    String sender = principal.getName();
    var payload = new ChatSignal(signal.to(), sender, signal.type(), signal.conversationId());
    String topic = "/queue/typing";
    messagingTemplate.convertAndSendToUser(signal.to(), topic, payload);
  }

  @MessageMapping("/chat.send")
  public void sendMessage(Principal principal, MessageCreateRequest request) {
    String sender = principal.getName();
    System.out.println();
    var payload = chatService.saveMessage(sender, request);
    String topic = "/queue/messages";
    messagingTemplate.convertAndSendToUser(request.receiver(), topic, payload);
    messagingTemplate.convertAndSendToUser(sender, topic, payload);
  }
}
