package com.nonglam.open_server.domain.chat;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nonglam.open_server.domain.auth.APIResponse;
import com.nonglam.open_server.domain.chat.conversation.dto.ConversationResponse;
import com.nonglam.open_server.domain.chat.conversation.dto.ConversationSummaryResponse;
import com.nonglam.open_server.security.CustomUserDetail;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/chat")
@RequiredArgsConstructor
public class ChatController {
  private final ChatService chatService;

  @GetMapping("/conversations/{id}")
  public ResponseEntity<APIResponse<ConversationResponse>> getConversationById(
      @PathVariable Long id,
      @AuthenticationPrincipal CustomUserDetail userDetail) {
    Long currentOpenerId = userDetail.getUser().getId();
    var response = chatService.getConversationById(id, currentOpenerId);
    return ResponseEntity.ok(APIResponse.success("conversation fetched", response));
  }

  @GetMapping("/conversations")
  public ResponseEntity<APIResponse<List<ConversationSummaryResponse>>> getConversationList(
      @AuthenticationPrincipal CustomUserDetail userDetail) {
    Long currentOpenerId = userDetail.getUser().getId();
    var response = chatService.getConversationSummaryList(currentOpenerId);
    return ResponseEntity.ok(APIResponse.success("conversationList fetched", response));
  }

  @PostMapping("/conversations")
  public ResponseEntity<APIResponse<ConversationSummaryResponse>> getOrCreateConversation(
      @AuthenticationPrincipal CustomUserDetail userDetail,
      @RequestParam Long targetId) {
    Long currentOpenerId = userDetail.getUser().getId();
    var response = chatService.getOrCreateConversation(currentOpenerId, targetId);
    return ResponseEntity.ok(APIResponse.success("conversate created", response));
  }

}
