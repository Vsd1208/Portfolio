package com.ecommerce.model.dto;

import com.ecommerce.model.enums.Role;

import java.time.LocalDateTime;

public record UserResponse(Long id, String email, String name, Role role, LocalDateTime createdAt) {
}
