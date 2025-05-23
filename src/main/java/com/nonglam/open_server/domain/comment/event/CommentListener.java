package com.nonglam.open_server.domain.comment.event;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentListener {
  SimpMessagingTemplate messagingTemplate;
  ObjectMapper objectMapper;

  @Async
  @EventListener
  public void hanldeCommentCreated(CommentCreatedEvent event) {
    String topic = "/topic/posts/" + event.postId() + "/comments";
    try {
      String payload = objectMapper.writeValueAsString(event);
      messagingTemplate.convertAndSend(topic, payload);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }
}
