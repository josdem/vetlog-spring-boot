package com.jos.dem.vetlog.service;

import com.jos.dem.vetlog.binder.UserBinder;
import com.jos.dem.vetlog.command.Command;
import com.jos.dem.vetlog.model.User;
import com.jos.dem.vetlog.repository.UserRepository;
import com.jos.dem.vetlog.service.impl.UserServiceImpl;
import com.jos.dem.vetlog.util.UserContextHolderProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
class UserServiceTest {

  private static final String USERNAME = "josdem";
  private static final String EMAIL = "contact@josdem.io";
  private UserService service;
  private User user = new User();

  @Mock private UserBinder userBinder;
  @Mock private UserRepository userRepository;
  @Mock private RecoveryService recoveryService;
  @Mock private UserContextHolderProvider provider;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    service = new UserServiceImpl(userBinder, userRepository, recoveryService, provider);
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
    when(userRepository.findByEmail(EMAIL)).thenReturn(user);
    User result = service.getByEmail(EMAIL);
    assertEquals(user, result);
  }

  @Test
  @DisplayName("saving an user")
  void shouldSaveAnUser(TestInfo testInfo) {
    log.info("Running: {}", testInfo.getDisplayName());
    Command command = mock(Command.class);
    final User user = new User();
    user.setEmail(EMAIL);
    when(userBinder.bindUser(command)).thenReturn(user);

    final User result = service.save(command);

    verify(userRepository).save(user);
    assertEquals(user, result);
  }

  @Test
  @DisplayName("getting current user")
  void shouldGetCurrentUser(TestInfo testInfo) {
    log.info("Running: {}", testInfo.getDisplayName());
    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(USERNAME);
    when(provider.getAuthentication()).thenReturn(authentication);
    when(userRepository.findByUsername(USERNAME)).thenReturn(user);
    assertEquals(user, service.getCurrentUser());
  }
}
