package com.jos.dem.vetlog.service;

import com.jos.dem.vetlog.enums.Role;
import com.jos.dem.vetlog.model.User;
import com.jos.dem.vetlog.service.impl.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Slf4j
class UserDetailsServiceTest {

  private UserDetailsServiceImpl service;

  @Mock private UserService userService;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    service = new UserDetailsServiceImpl(userService);
  }

  @Test
  @DisplayName("loading user by username")
  void shouldLoadUserByUsername(TestInfo testInfo) {
    log.info("Running: {}", testInfo.getDisplayName());
    User user = new User();
    user.setUsername("josdem");
    user.setPassword("password");
    user.setEnabled(true);
    user.setAccountNonExpired(true);
    user.setAccountNonLocked(true);
    user.setCredentialsNonExpired(true);
    user.setRole(Role.USER);
    when(userService.getByUsername("josdem")).thenReturn(user);

    org.springframework.security.core.userdetails.User result =
        service.loadUserByUsername("josdem");

    assertEquals(user.getUsername(), result.getUsername());
    assertEquals(user.getPassword(), result.getPassword());
    assertEquals(user.getEnabled(), result.isEnabled());
  }
}
