package com.nonglam.open_server.domain.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.nonglam.open_server.domain.auth.dto.UserMapper;
import com.nonglam.open_server.domain.auth.dto.request.LoginRequest;
import com.nonglam.open_server.domain.auth.dto.request.RegisterRequest;
import com.nonglam.open_server.domain.auth.dto.request.TokenRequest;
import com.nonglam.open_server.domain.auth.dto.response.LoginResponse;
import com.nonglam.open_server.domain.auth.dto.response.UserResponse;
import com.nonglam.open_server.domain.user.Opener;
import com.nonglam.open_server.domain.user.OpenerRepository;
import com.nonglam.open_server.domain.user.User;
import com.nonglam.open_server.exception.ApiException;
import com.nonglam.open_server.security.CustomUserDetail;
import com.nonglam.open_server.security.JwtUtil;
import com.nonglam.open_server.shared.ErrorCode;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthService {
  final AuthRepository authRepository;
  final UserMapper userMapper;
  final PasswordEncoder passwordEncoder;
  final AuthenticationManager authenticationManager;
  final JwtUtil jwtUtil;
  final OpenerRepository openerRepository;
  @Value("${google.client.id}")
  String CLIENT_ID;

  public LoginResponse login(LoginRequest request) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.email(), request.password()));
    CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
    var user = customUserDetail.getUser();
    if (user.isBlocked()) {
      throw new ApiException("opener's account blocked", ErrorCode.OPENER_BLOCKED);
    }
    var userResponse = userMapper.toUserResponse(user);
    String accessToken = jwtUtil.generateAccessToken(user.getEmail(),
        Map.of("role", user.getRole(), "username", user.getUsername()));
    String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
    return new LoginResponse(userResponse, accessToken, refreshToken);
  }

  public LoginResponse googleLogin(TokenRequest request) {
    var jsonFactory = new GsonFactory();
    var transport = new NetHttpTransport();
    var audience = Collections.singleton(CLIENT_ID);
    GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
        .setAudience(audience)
        .build();
    try {
      var idToken = verifier.verify(request.token());
      if (idToken == null) {
        throw new RuntimeException("Invalid ID token");
      }
      var payload = idToken.getPayload();
      Boolean emailVerified = (Boolean) payload.get("email_verified");
      if (!Boolean.TRUE.equals(emailVerified)) {
        throw new RuntimeException("Email not verified by Google");
      }
      var email = payload.getEmail();
      var displayName = (String) payload.get("name");
      var username = email.substring(0, email.lastIndexOf("@"));
      var avatarUrl = (String) payload.get("picture");
      var opener = openerRepository
          .findByEmail(email)
          .orElseGet(() -> {
            var newOpener = new Opener();
            newOpener.setEmail(email);
            newOpener.setDisplayName(displayName);
            newOpener.setUsername(username);
            newOpener.setAvatarUrl(avatarUrl);
            newOpener.setRegisteredWithGoogle(true);
            return openerRepository.save(newOpener);
          });

      if (opener.isBlocked()) {
        throw new ApiException("opener's account blocked", ErrorCode.OPENER_BLOCKED);
      }
      var userResponse = userMapper.toUserResponse(opener);
      String accessToken = jwtUtil.generateAccessToken(opener.getEmail(),
          Map.of("role", opener.getRole(), "username", opener.getUsername()));
      String refreshToken = jwtUtil.generateRefreshToken(opener.getEmail());
      return new LoginResponse(userResponse, accessToken, refreshToken);
    } catch (GeneralSecurityException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  public UserResponse register(RegisterRequest registerRequest) {
    if (authRepository.existsByEmail(registerRequest.email()))
      throw new ApiException("Email already exists", ErrorCode.EMAIL_EXISTS);
    if (authRepository.existsByUsername(registerRequest.username()))
      throw new ApiException("Username already exists", ErrorCode.USERNAME_EXISTS);
    User user = userMapper.toOpener(registerRequest);
    user.setPassword(passwordEncoder.encode(registerRequest.password()));
    user = authRepository.save(user);
    return userMapper.toUserResponse(user);
  }

}
