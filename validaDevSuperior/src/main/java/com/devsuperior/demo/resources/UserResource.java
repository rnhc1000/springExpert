package com.devsuperior.demo.resources;

import com.devsuperior.demo.dto.UserDTO;
import com.devsuperior.demo.entities.User;
import com.devsuperior.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserResource {

  @Autowired
  private UserService userService;

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping(value = "/users")
  public ResponseEntity<Page<UserDTO>> getUsers(Pageable pageable) {
    Page<UserDTO> users = userService.getAllUsers(pageable);

    return ResponseEntity.ok().body(users);
  }
}
