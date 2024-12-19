package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.services.exceptions.ForbiddenException;
import com.devsuperior.dscommerce.tests.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class AuthServiceTest {

  @InjectMocks
  private AuthService authService;

  @Mock
  private UserService userService;


  private User admin, selfClient, otherClient;

  @BeforeEach
  void setUp() throws Exception {

    admin = UserFactory.createAdminUser();
    selfClient = UserFactory.createCustomClientUser(1L, "Bob");
    otherClient = UserFactory.createCustomClientUser(2L, "Ana");
  }
  @Test
  void validateSelfOrAdminShouldDoNothingWhenAdminLogged() {
    Mockito.when(userService.authenticated()).thenReturn(admin);
    Long userId = admin.getId();

    Assertions.assertDoesNotThrow( () -> {
      authService.validateSelfOrAdmin(userId);
    });
  }
  @Test
  void validateSelfOrAdminShouldDoNothingWhenSelfLogged() {
    Mockito.when(userService.authenticated()).thenReturn(selfClient);
    Long userId = selfClient.getId();

    Assertions.assertDoesNotThrow( () -> {
      authService.validateSelfOrAdmin(userId);
    });
  }

  @Test
  void validateSelfOrAdminThrowsForbiddenExceptionWhenClientOtherLogged() {
    Mockito.when(userService.authenticated()).thenReturn(selfClient);
    Long userId = otherClient.getId();

    Assertions.assertThrows(ForbiddenException.class, () -> {
      authService.validateSelfOrAdmin(userId);
    });
  }
}
