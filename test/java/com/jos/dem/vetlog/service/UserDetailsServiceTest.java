package com.jos.dem.vetlog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.jos.dem.vetlog.enums.Role;
import com.jos.dem.vetlog.exception.BusinessException;
import com.jos.dem.vetlog.model.User;
import com.jos.dem.vetlog.repository.UserRepository;
import com.jos.dem.vetlog.service.impl.UserDetailsServiceImpl;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
class UserDetailsServiceTest {

    private static final String USERNAME = "josdem";

    private UserDetailsServiceImpl service;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        service = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    @DisplayName("loading user by username")
    void shouldLoadUserByUsername(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        User user = new User();
        user.setUsername(USERNAME);
        user.setPassword("password");
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setRole(Role.USER);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        UserDetails result = service.loadUserByUsername(USERNAME);

        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getEnabled(), result.isEnabled());
    }

    @Test
    @DisplayName("not search for authorities since user does not exist")
    void shouldNotSearchForAuthoritiesDueToUserNotFound(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        assertThrows(BusinessException.class, () -> service.loadUserByUsername("thisUserDoesNotExist"));
    }
}
