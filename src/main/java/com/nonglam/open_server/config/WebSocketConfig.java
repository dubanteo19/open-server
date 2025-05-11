package com.nonglam.open_server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.nonglam.open_server.security.JwtChannelInterceptor;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  final JwtChannelInterceptor jwtChannelInterceptor;
  @Value("${frontend.allowed-origins}")
  String allowedOrigins;

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws")
        .setAllowedOrigins(allowedOrigins.split(","));
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(jwtChannelInterceptor);
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker("/topic");
    registry.setApplicationDestinationPrefixes("/app");
  }

}
