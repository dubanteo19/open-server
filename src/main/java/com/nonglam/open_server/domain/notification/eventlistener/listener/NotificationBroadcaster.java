package com.nonglam.open_server.domain.notification.eventlistener.listener;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.nonglam.open_server.domain.notification.eventlistener.event.NotificationEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationBroadcaster {
  private final SimpMessagingTemplate simpMessagingTemplate;

  @EventListener
  @Async
  public void handleNotificationEvent(NotificationEvent event) {
    String topic = "/queue/notifications";
    simpMessagingTemplate.convertAndSendToUser(event.receiver(), topic, event.message());
    ;
  }
}
