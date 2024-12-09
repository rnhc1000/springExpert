package com.devsuperior.demo.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "tb_user")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  private String username;

  @Column(unique = true)
  private String email;

  private String password;

  @CreationTimestamp
  private Instant createdAt;

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  private Instant updatedAt;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "tb_user_role",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();


  public User() {}

  public User(
      Long id, String username, String email,
      String password, Instant createdAt, Instant updatedAt,
      Set<Role> roles) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.password = password;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.roles = roles;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  /**
   * @return roles
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles;
  }

  /**
   * @return password
   */
  @Override
  public String getPassword() {
    return password;
  }

  /**
   * @return email
   */
  @Override
  public String getUsername() {
    return email;
  }

  /**
   * @return true
   */
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  /**
   * @return true
   */
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  /**
   * @return true
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  /**
   * @return true
   */
  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    User user = (User) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  public void addRole(Role role) {
    roles.add(role);
  }

  public boolean hasRole(String roleName) {

    for(Role role : roles) {
      if(role.getAuthority().equals(roleName)) {
        return true;
      }
    }

    return false;
  }
}
