package com.devsuperior.demo.dto;

import com.devsuperior.demo.entities.User;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class UserDTO {
  private Long id;
  private String username;
  private String email;
  private Instant createdAt;
  private Instant updatedAt;
  Set<RoleDTO> roles = new HashSet<>();

  public Set<RoleDTO> getRoles() {
    return roles;
  }

  public UserDTO() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  public UserDTO(User entity) {
    id = entity.getId();
    username = entity.getUsername();
    email = entity.getEmail();
    createdAt = entity.getCreatedAt();
    updatedAt = entity.getUpdatedAt();
    entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
  }
}
