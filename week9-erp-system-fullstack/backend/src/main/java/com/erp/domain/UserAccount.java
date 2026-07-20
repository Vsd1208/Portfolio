package com.erp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
public class UserAccount {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotBlank @Column(unique = true)
  private String username;
  @Email @NotBlank @Column(unique = true)
  private String email;
  @NotBlank
  private String passwordHash;
  @Enumerated(EnumType.STRING)
  private Role role = Role.ADMIN;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public String getPasswordHash() { return passwordHash; }
  public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
  public Role getRole() { return role; }
  public void setRole(Role role) { this.role = role; }
}
