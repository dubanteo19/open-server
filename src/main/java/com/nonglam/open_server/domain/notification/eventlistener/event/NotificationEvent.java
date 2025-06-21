package com.nonglam.open_server.domain.notification.eventlistener.event;

public record NotificationEvent(
    String receiver,
    String message) {
}
