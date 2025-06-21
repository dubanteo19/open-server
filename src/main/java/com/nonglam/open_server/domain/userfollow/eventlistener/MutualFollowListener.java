package com.nonglam.open_server.domain.userfollow.eventlistener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.nonglam.open_server.domain.chat.ChatService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MutualFollowListener {
  private final ChatService chatService;

  @EventListener
  public void handleMutalFollow(MutualFollowEvent event) {
    chatService.getOrCreateConversation(event.followerId(), event.followedId());
  }
}
