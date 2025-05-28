package com.nonglam.open_server.domain.chat;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nonglam.open_server.domain.auth.APIResponse;
import com.nonglam.open_server.domain.chat.conversation.dto.ConversationResponse;
import com.nonglam.open_server.domain.chat.message.dto.MessageCreateRequest;
import com.nonglam.open_server.domain.chat.message.dto.MessageResponse;
import com.nonglam.open_server.security.CustomUserDetail;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/chat")
@RequiredArgsConstructor
public class ChatController {
  private final ChatService chatService;

  @GetMapping("/conversations/{id}/messages")
  public ResponseEntity<APIResponse<List<MessageResponse>>> getMessagesByConversationId(
      @PathVariable Long id,
      @AuthenticationPrincipal CustomUserDetail userDetail) {
    Long currentOpenerId = userDetail.getUser().getId();
    var response = chatService.getMessagesByConversationId(id, currentOpenerId);
    return ResponseEntity.ok(APIResponse.success("messages feted", response));
  }

  @PostMapping("/conversations/{id}/messages")
  public ResponseEntity<APIResponse<MessageResponse>> sendMessage(
      @PathVariable Long id,
      @RequestBody MessageCreateRequest request,
      @AuthenticationPrincipal CustomUserDetail userDetail) {
    Long currentOpenerId = userDetail.getUser().getId();
    Long receiverId = request.receiverId();
    String content = request.content();
    var response = chatService.sendMessage(currentOpenerId, receiverId, content);
    return ResponseEntity.ok(APIResponse.success("message sent", response));
  }

  @GetMapping("/conversations/{id}")
  public ResponseEntity<APIResponse<ConversationResponse>> getConversationById(
      @PathVariable Long id,
      @AuthenticationPrincipal CustomUserDetail userDetail) {
    Long currentOpenerId = userDetail.getUser().getId();
    var response = chatService.getConversationById(id, currentOpenerId);
    return ResponseEntity.ok(APIResponse.success("conversate created", response));
  }

  @GetMapping("/conversations")
  public ResponseEntity<APIResponse<List<ConversationResponse>>> getConversations(
      @AuthenticationPrincipal CustomUserDetail userDetail) {
    Long currentOpenerId = userDetail.getUser().getId();
    var response = chatService.getConversations(currentOpenerId);
    return ResponseEntity.ok(APIResponse.success("conversate created", response));
  }

  @PostMapping("/conversations")
  public ResponseEntity<APIResponse<ConversationResponse>> createConversation(
      @AuthenticationPrincipal CustomUserDetail userDetail,
      @RequestParam Long targetId) {
    Long currentOpenerId = userDetail.getUser().getId();
    var response = chatService.createConversation(currentOpenerId, targetId);
    return ResponseEntity.ok(APIResponse.success("conversate created", response));
  }

}
