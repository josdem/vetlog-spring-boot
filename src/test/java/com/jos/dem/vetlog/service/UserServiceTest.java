package com.jos.dem.vetlog.service;

import com.jos.dem.vetlog.binder.UserBinder;
import com.jos.dem.vetlog.model.User;
import com.jos.dem.vetlog.repository.UserRepository;
import com.jos.dem.vetlog.service.impl.UserServiceImpl;
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
class UserServiceTest {

  public static final String USERNAME = "josdem";
  private UserService service;
  private User user = new User();

  @Mock private UserBinder userBinder;
  @Mock private UserRepository userRepository;
  @Mock private RecoveryService recoveryService;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    service = new UserServiceImpl(userBinder, userRepository, recoveryService);
  }

  @Test
  @DisplayName("getting user by username")
  void shouldGetUserByUsername(TestInfo testInfo) {
    log.info("Running: {}", testInfo.getDisplayName());
    when(userRepository.findByUsername(USERNAME)).thenReturn(user);
    User result = service.getByUsername(USERNAME);
    assertEquals(user, result);
  }

  @Test
  @DisplayName("getting user by email")
  void shouldGetUserByEmail(TestInfo testInfo) {
    log.info("Running: {}", testInfo.getDisplayName());
    final String email = "contact@josdem.io";
    when(userRepository.findByEmail(email)).thenReturn(user);
    User result = service.getByEmail(email);
    assertEquals(user, result);
  }
}
