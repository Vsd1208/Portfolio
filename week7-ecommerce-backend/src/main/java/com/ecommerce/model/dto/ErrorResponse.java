package com.ecommerce.model.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(LocalDateTime timestamp, int status, String message,
                            String path, Map<String, String> errors) {
}
