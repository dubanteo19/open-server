package com.nonglam.open_server.security;

import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {

  private final JwtUtil jwtUtil;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
      List<String> authHeaders = accessor.getNativeHeader("Authorization");
      if (authHeaders != null && !authHeaders.isEmpty()) {
        String token = authHeaders.get(0).replace("Bearer ", "");
        if (jwtUtil.isTokenValid(token, false)) {
          String email = jwtUtil.extractEmail(token);
          accessor.setUser(new StompPrincipal(email));
        } else {
          throw new IllegalArgumentException("Invalid token");
        }
      }
    }

    return message;
  }

}
