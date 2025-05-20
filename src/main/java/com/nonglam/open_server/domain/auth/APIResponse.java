package com.nonglam.open_server.domain.auth;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

import com.nonglam.open_server.shared.ErrorCode;

@Getter
public class APIResponse<T> {
  private boolean success;
  private String message;
  private T data;
  private List<String> errors;
  private ErrorCode errorCode;
  private LocalDateTime timestamp = LocalDateTime.now();

  public APIResponse(String message, boolean success, T data, List<String> errors, ErrorCode errorCode) {
    this.message = message;
    this.success = success;
    this.data = data;
    this.errors = errors;
    this.errorCode = errorCode;
  }

  public static <T> APIResponse<T> success(String message) {
    return new APIResponse(message, true, null, null, null);
  }

  public static <T> APIResponse<T> success(String message, T data) {
    return new APIResponse(message, true, data, null, null);
  }

  public static <T> APIResponse<T> error(String message, List<String> errors, ErrorCode errorCode) {
    return new APIResponse(message, false, null, errors, errorCode);
  }
}
