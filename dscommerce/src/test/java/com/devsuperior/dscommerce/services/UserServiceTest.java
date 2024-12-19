package com.devsuperior.dscommerce.services;


import com.devsuperior.dscommerce.dto.UserDTO;
import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.projections.UserDetailsProjection;
import com.devsuperior.dscommerce.repositories.UserRepository;
import com.devsuperior.dscommerce.tests.UserDetailsFactory;
import com.devsuperior.dscommerce.tests.UserFactory;
import com.devsuperior.dscommerce.utils.CustomUserUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  private String existingUsername, nonExistingUsername;
  private User user;
  private List<UserDetailsProjection> userDetails;

  @Mock
  private CustomUserUtil customUserUtil;


  @BeforeEach
  void setUp() throws Exception {
    existingUsername = "maria@gmail.com";
    nonExistingUsername = "user@gmail.com";

    user = UserFactory.createCustomClientUser(1L, existingUsername);
    userDetails = UserDetailsFactory.createCustomAdminClientUser(existingUsername);

    Mockito.when(userRepository.searchUserAndRolesByEmail(existingUsername)).thenReturn(userDetails);
    Mockito.when(userRepository.searchUserAndRolesByEmail(nonExistingUsername)).thenReturn(new ArrayList<>());

    Mockito.when(userRepository.findByEmail(existingUsername)).thenReturn(Optional.of(user));
    Mockito.when(userRepository.findByEmail(nonExistingUsername)).thenReturn(Optional.empty());


  }

  @Test
  void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() {

    UserDetails result = userService.loadUserByUsername(existingUsername);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(result.getUsername(), existingUsername);
  }

  @Test
  void loadUserByUsernameShouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {

   Assertions.assertThrows(UsernameNotFoundException.class, () -> {
      userService.loadUserByUsername(nonExistingUsername);
    });

  }

  @Test
  void authenticatedShouldReturnUserWhenUserExists() {
    Mockito.when(customUserUtil.getLoggedUsername()).thenReturn(existingUsername);

    User result = userService.authenticated();

    Assertions.assertNotNull(result);
    Assertions.assertEquals(result.getUsername(), existingUsername);
  }

  @Test
  void authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {

    Mockito.doThrow(ClassCastException.class).when(customUserUtil).getLoggedUsername();

    Assertions.assertThrows(UsernameNotFoundException.class, () -> {
      userService.authenticated();
    });
  }

  @Test
  void getMeShouldReturnUSerDTOWhenUserAuthenticated() {

    UserService spyUserService = Mockito.spy(userService);
    Mockito.doReturn(user).when(spyUserService).authenticated();
//    Mockito.doReturn(user).when(userService).authenticated();
    UserDTO result = spyUserService.getMe();

    Assertions.assertNotNull(result);
    Assertions.assertEquals(result.getEmail(), existingUsername);
  }

  @Test
  void getMeShouldThrowUserNotFoundExceptionWhenUserNotAuthenticated() {

    UserService spyUserService = Mockito.spy(userService);
    Mockito.doThrow(UsernameNotFoundException.class).when(spyUserService).authenticated();

    Assertions.assertThrows(UsernameNotFoundException.class, () -> {
        UserDTO result = spyUserService.getMe();
      });
  }

}
