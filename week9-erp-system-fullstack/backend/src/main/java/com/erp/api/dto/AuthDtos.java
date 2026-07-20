package com.erp.api.dto;

import com.erp.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public final class AuthDtos {
  private AuthDtos() {}

  public record LoginRequest(@NotBlank String username, @NotBlank String password) {}
  public record RegisterRequest(@NotBlank String username, @Email @NotBlank String email, @NotBlank String password, @NotNull Role role) {}
  public record AuthResponse(String token, String username, Role role) {}
}
