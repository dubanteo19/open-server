package com.nonglam.open_server.exception;

import com.nonglam.open_server.shared.ErrorCode;

public class ApiException extends RuntimeException {
  final ErrorCode errorCode;

  public ApiException(String message, ErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  public ErrorCode getErrorCode() {
    return this.errorCode;
  }
}
