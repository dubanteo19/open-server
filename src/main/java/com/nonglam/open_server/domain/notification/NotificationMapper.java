package com.nonglam.open_server.domain.notification;

import org.springframework.stereotype.Component;

import com.nonglam.open_server.domain.notification.dto.response.NotificationResponse;

@Component
public class NotificationMapper {
  public NotificationResponse toResponse(Notification notification) {
    return new NotificationResponse(
        notification.getId(),
        notification.getContent(),
        notification.isRead(),
        notification.getCreatedAt());
  }

}
