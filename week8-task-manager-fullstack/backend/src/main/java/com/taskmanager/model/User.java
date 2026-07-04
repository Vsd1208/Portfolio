package com.taskmanager.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
@Entity @Table(name="app_users")
public class User {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false) private String name;
  @Column(nullable=false,unique=true) private String email;
  @JsonIgnore @Column(nullable=false) private String password;
  public Long getId(){return id;} public String getName(){return name;} public void setName(String v){name=v;}
  public String getEmail(){return email;} public void setEmail(String v){email=v;}
  public String getPassword(){return password;} public void setPassword(String v){password=v;}
}
