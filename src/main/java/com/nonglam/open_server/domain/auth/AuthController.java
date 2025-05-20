package com.nonglam.open_server.domain.auth;

import com.nonglam.open_server.domain.auth.dto.request.LoginRequest;
import com.nonglam.open_server.domain.auth.dto.request.RegisterRequest;
import com.nonglam.open_server.domain.auth.dto.request.TokenRequest;
import com.nonglam.open_server.domain.auth.dto.response.LoginResponse;
import com.nonglam.open_server.domain.auth.dto.response.UserResponse;
import com.nonglam.open_server.security.CustomUserDetail;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/auth")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class AuthController {
  AuthService authService;

  @GetMapping("/me")
  public ResponseEntity<APIResponse<UserResponse>> me(@AuthenticationPrincipal CustomUserDetail userDetail) {
    var user = userDetail.getUser();
    var userResponse = new UserResponse(user.getId(), user.getUsername(), user.getDisplayName(), user.getAvatarUrl());
    return ResponseEntity.ok(APIResponse.success("Get current user successfully", userResponse));
  }

  @PostMapping("/google")
  public ResponseEntity<APIResponse<LoginResponse>> googleLogin(@RequestBody TokenRequest request) {
    return ResponseEntity.ok(APIResponse.success("login successfully", authService.googleLogin(request)));
  }

  @PostMapping("/login")
  public ResponseEntity<APIResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
    return ResponseEntity.ok(APIResponse.success("login successfully", authService.login(request)));
  }

  @PostMapping("/register")
  public ResponseEntity<APIResponse<UserResponse>> register(@RequestBody RegisterRequest request) {
    var response = authService.register(request);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(APIResponse.success("Registered successfully", response));
  }
}
