package com.nonglam.open_server.security;

import java.util.Collections;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

  private final JwtUtil jwtUtil;

  public JwtChannelInterceptor(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
      String token = null;
      String authHeader = accessor.getFirstNativeHeader("Authorization");
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        token = authHeader.substring(7);
      } else {
        // Optional: fallback to custom header
        token = accessor.getFirstNativeHeader("access_token");
      }

      if (token != null && jwtUtil.isTokenValid(token, false)) {
        String username = jwtUtil.extractUsername(token, false);
        accessor.setUser(new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList()));
      } else {
        throw new IllegalArgumentException("Invalid or missing JWT token");
      }
    }

    return message;
  }
}
