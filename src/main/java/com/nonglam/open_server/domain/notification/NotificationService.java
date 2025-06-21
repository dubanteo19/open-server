package com.nonglam.open_server.domain.notification;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nonglam.open_server.domain.notification.dto.response.NotificationResponse;
import com.nonglam.open_server.domain.notification.eventlistener.event.NotificationEvent;
import com.nonglam.open_server.domain.user.Opener;
import com.nonglam.open_server.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;
  private final ApplicationEventPublisher eventPublisher;

  public Notification findById(Long id) {
    return notificationRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("notification  not found"));
  }

  public void markAsRead(Long notificationId) {
    var currentNotification = findById(notificationId);
    currentNotification.setRead(true);
    notificationRepository.save(currentNotification);
  };

  public void saveNotification(String content, Opener opener) {
    var notification = new Notification(content, opener);
    notificationRepository.save(notification);
    eventPublisher.publishEvent(new NotificationEvent(opener.getUsername(), content));
  };

  public List<NotificationResponse> getNotificationByOpenerId(Long id) {
    var sort = Sort.by("createdAt").descending();
    var notifications = notificationRepository.findByOpenerId(id, sort);
    return notifications.stream()
        .map(notificationMapper::toResponse).toList();
  }

  public Long getUnreadNoficationCount(Long currentOpenerId) {
    return notificationRepository.countByOpenerIdAndIsRead(currentOpenerId, false);
  }

  public List<NotificationResponse> getUnreadNotificationByOpenerId(Long id) {
    var sort = Sort.by("createdAt").descending();
    var notifications = notificationRepository.findByOpenerIdAndIsRead(id, false, sort);
    return notifications.stream()
        .map(notificationMapper::toResponse).toList();
  }

  public void markAsReadAll(Long currentOpenerId) {
    var notifications = notificationRepository.findAllByOpenerId(currentOpenerId);
    if (!notifications.isEmpty()) {
      notifications.forEach(Notification::markAsRead);
      notificationRepository.saveAll(notifications);
    }
  }
}
