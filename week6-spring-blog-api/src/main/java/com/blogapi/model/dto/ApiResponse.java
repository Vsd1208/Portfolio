package com.blogapi.model.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class ApiResponse {
    private final LocalDateTime timestamp;
    private final int status;
    private final String message;
    private final String path;
    private final Map<String, String> errors;

    public ApiResponse(int status, String message, String path, Map<String, String> errors) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.path = path;
        this.errors = errors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
