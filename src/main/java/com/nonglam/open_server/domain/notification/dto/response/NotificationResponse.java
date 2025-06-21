package com.nonglam.open_server.domain.notification.dto.response;

import java.time.LocalDateTime;

public record NotificationResponse(
    Long id,
    String content,
    boolean isRead,
    LocalDateTime createdAt) {

}
