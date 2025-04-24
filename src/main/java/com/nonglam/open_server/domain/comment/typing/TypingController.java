package com.nonglam.open_server.domain.comment.typing;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class TypingController {

  SimpMessagingTemplate messagingTemplate;
  Gson gson;

  @MessageMapping("/typing/{postId}")
  public void openerTyping(@DestinationVariable Long postId, @Payload String payload) {
    String topic = "/topic/typing/" + postId;
    String username = gson.fromJson(payload, String.class);
    TypingEvent event = new TypingEvent(username);
    String outgoingPayload = gson.toJson(event);
    messagingTemplate.convertAndSend(topic, outgoingPayload);
  }

}
