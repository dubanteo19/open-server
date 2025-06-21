package com.nonglam.open_server.security;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

  private final JwtUtil jwtUtil;

  @Override
  public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
      WebSocketHandler wsHandler, Map<String, Object> attributes) {
    if (request instanceof ServletServerHttpRequest servletRequest) {
      HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
      String token = httpServletRequest.getParameter("token");
      if (!jwtUtil.isTokenValid(token, false))
        return false;
      String email = jwtUtil.extractUsername(token);
      attributes.put("user", new StompPrincipal(email));
      return true;
    }
    response.setStatusCode(HttpStatus.UNAUTHORIZED);
    return false;
  }

  @Override
  public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
      WebSocketHandler wsHandler, Exception exception) {
  }
}
