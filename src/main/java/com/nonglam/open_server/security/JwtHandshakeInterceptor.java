package com.nonglam.open_server.security;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
@Slf4j
public class JwtHandshakeInterceptor implements HandshakeInterceptor {
  JwtUtil jwtUtil;

  @Override
  public void afterHandshake(ServerHttpRequest arg0, ServerHttpResponse arg1, WebSocketHandler arg2, Exception arg3) {
  }

  private String extractToken(ServerHttpRequest request) {
    String authHeader = request.getHeaders().getFirst("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      return authHeader.substring(7);
    }
    String query = request.getURI().getQuery();
    if (query != null && query.contains("access_token")) {
      return query.split("access_token=")[1].split("&")[0];
    }
    return null;
  }

  @Override
  public boolean beforeHandshake(ServerHttpRequest request,
      ServerHttpResponse response, WebSocketHandler wsHandler,
      Map<String, Object> attributes) throws Exception {
    String token = extractToken(request);
    if (jwtUtil.isTokenValid(token, false)) {
      String username = jwtUtil.extractUsername(token, false);
      attributes.put("username", username);
      return true;
    }
    response.setStatusCode(HttpStatus.UNAUTHORIZED);
    return false;
  }

}
