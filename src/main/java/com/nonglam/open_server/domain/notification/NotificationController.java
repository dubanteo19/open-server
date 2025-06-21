package com.nonglam.open_server.domain.notification;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nonglam.open_server.domain.auth.APIResponse;
import com.nonglam.open_server.domain.notification.dto.response.NotificationResponse;
import com.nonglam.open_server.security.CustomUserDetail;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
  private final NotificationService notificationService;

  @PostMapping
  public ResponseEntity<APIResponse<Void>> markAsReadAll(
      @AuthenticationPrincipal CustomUserDetail userDetail
  ) {
    Long currentOpenerId = userDetail.getUser().getId();
    notificationService.markAsReadAll(currentOpenerId);
    return ResponseEntity.ok(APIResponse.success("all notifications marked as read"));
  }

  @PostMapping("/{id}")
  public ResponseEntity<APIResponse<Void>> markAsRead(
      @AuthenticationPrincipal CustomUserDetail userDetail,
      @PathVariable Long id) {
    notificationService.markAsRead(id);
    return ResponseEntity.ok(APIResponse.success("notification marked as read"));
  }

  @GetMapping("/unread-count")
  public ResponseEntity<APIResponse<Long>> getUnreadNoficationCount(
      @AuthenticationPrincipal CustomUserDetail userDetail) {
    Long currentOpenerId = userDetail.getUser().getId();
    var response = notificationService.getUnreadNoficationCount(currentOpenerId);
    return ResponseEntity.ok(APIResponse.success("unread notifications fetched", response));
  }

  @GetMapping("/unread")
  public ResponseEntity<APIResponse<List<NotificationResponse>>> getUnreadNoficationList(
      @AuthenticationPrincipal CustomUserDetail userDetail) {
    Long currentOpenerId = userDetail.getUser().getId();
    var response = notificationService.getUnreadNotificationByOpenerId(currentOpenerId);
    return ResponseEntity.ok(APIResponse.success("notifications fetched", response));
  }

  @GetMapping
  public ResponseEntity<APIResponse<List<NotificationResponse>>> getNoficationList(
      @AuthenticationPrincipal CustomUserDetail userDetail) {
    Long currentOpenerId = userDetail.getUser().getId();
    var response = notificationService.getNotificationByOpenerId(currentOpenerId);
    return ResponseEntity.ok(APIResponse.success("notifications fetched", response));
  }
}
