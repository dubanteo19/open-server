package com.nonglam.open_server.exception;

import com.nonglam.open_server.domain.auth.APIResponse;
import com.nonglam.open_server.shared.ErrorCode;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(Exception.class)
  public ResponseEntity<APIResponse<Object>> handleAllExceptions(Exception exception) {
    var response = APIResponse.error("Something went wrong", List.of(exception.getMessage()), null);
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<APIResponse<Object>> handleApiException(ApiException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(APIResponse.error(exception.getMessage(), List.of(), exception.getErrorCode()));
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<APIResponse<Object>> handleBadCredentialsException(
      BadCredentialsException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(APIResponse.error(exception.getMessage(), List.of(), ErrorCode.BAD_CREDENTIAL));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<APIResponse<Object>> handleIllegalArgument(IllegalArgumentException ex) {
    APIResponse<Object> response = APIResponse.error(
        "Invalid argument",
        List.of(ex.getMessage()), null);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
}
