package com.erp.api;

import com.erp.api.dto.AuthDtos.AuthResponse;
import com.erp.api.dto.AuthDtos.LoginRequest;
import com.erp.api.dto.AuthDtos.RegisterRequest;
import com.erp.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  AuthResponse register(@Valid @RequestBody RegisterRequest request) {
    return authService.register(request);
  }

  @PostMapping("/login")
  AuthResponse login(@Valid @RequestBody LoginRequest request) {
    return authService.login(request);
  }
}
