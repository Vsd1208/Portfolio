package com.erp.service;

import com.erp.api.dto.AuthDtos.AuthResponse;
import com.erp.api.dto.AuthDtos.LoginRequest;
import com.erp.api.dto.AuthDtos.RegisterRequest;
import com.erp.domain.UserAccount;
import com.erp.repository.UserAccountRepository;
import com.erp.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final UserAccountRepository users;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthService(UserAccountRepository users, PasswordEncoder passwordEncoder, JwtService jwtService) {
    this.users = users;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  public AuthResponse register(RegisterRequest request) {
    if (users.existsByUsername(request.username()) || users.existsByEmail(request.email())) {
      throw new IllegalArgumentException("Username or email already exists");
    }
    var user = new UserAccount();
    user.setUsername(request.username());
    user.setEmail(request.email());
    user.setRole(request.role());
    user.setPasswordHash(passwordEncoder.encode(request.password()));
    users.save(user);
    return new AuthResponse(jwtService.issue(user), user.getUsername(), user.getRole());
  }

  public AuthResponse login(LoginRequest request) {
    var user = users.findByUsername(request.username()).orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
    if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
      throw new IllegalArgumentException("Invalid credentials");
    }
    return new AuthResponse(jwtService.issue(user), user.getUsername(), user.getRole());
  }
}
