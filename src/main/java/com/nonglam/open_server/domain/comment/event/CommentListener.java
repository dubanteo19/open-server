package com.nonglam.open_server.domain.comment.event;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentListener {
  SimpMessagingTemplate messagingTemplate;
  Gson gson;

  @Async
  @EventListener
  public void hanldeCommentCreated(CommentCreatedEvent event) {
    String topic = "/topic/posts/" + event.postId() + "/comments";
    String payload = gson.toJson(event);
    System.out.println(topic);
    System.out.println(payload);
    messagingTemplate.convertAndSend(topic, payload);
    System.out.println("send event successfully");
  }
}
