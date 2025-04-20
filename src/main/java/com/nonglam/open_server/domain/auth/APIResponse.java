package com.nonglam.open_server.domain.auth;


import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class APIResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private List<String> errors;
    private LocalDateTime timestamp = LocalDateTime.now();

    public APIResponse(String message, boolean success, T data, List<String> errors) {
        this.message = message;
        this.success = success;
        this.data = data;
        this.errors = errors;
    }

    public static <T> APIResponse<T> success(String message) {
        return new APIResponse(message, true, null, null);
    }

    public static <T> APIResponse<T> success(String message, T data) {
        return new APIResponse(message, true, data, null);
    }

    public static <T> APIResponse<T> error(String message, List<String> errors) {
        return new APIResponse(message, false, null, errors);
    }
}
