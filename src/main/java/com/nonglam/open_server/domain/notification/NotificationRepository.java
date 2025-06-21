package com.nonglam.open_server.domain.notification;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

  List<Notification> findByOpenerId(Long id, Sort sort);

  List<Notification> findByOpenerIdAndIsRead(Long id, boolean isRead, Sort sort);

  Long countByOpenerIdAndIsRead(Long currentOpenerId, boolean isRead);

  List<Notification> findAllByOpenerId(Long currentOpenerId);

}
