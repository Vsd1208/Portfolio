package com.erp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
public class BusinessPartner {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Enumerated(EnumType.STRING)
  private PartnerType type;
  @NotBlank
  private String name;
  @Email @NotBlank
  private String email;
  @NotBlank
  private String phone;
  @NotBlank
  private String address;
  private String gstin;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public PartnerType getType() { return type; }
  public void setType(PartnerType type) { this.type = type; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }
  public String getAddress() { return address; }
  public void setAddress(String address) { this.address = address; }
  public String getGstin() { return gstin; }
  public void setGstin(String gstin) { this.gstin = gstin; }
}
