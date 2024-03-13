package com.jos.dem.vetlog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jos.dem.vetlog.command.ChangePasswordCommand;
import com.jos.dem.vetlog.command.MessageCommand;
import com.jos.dem.vetlog.exception.UserNotFoundException;
import com.jos.dem.vetlog.exception.VetlogException;
import com.jos.dem.vetlog.model.RegistrationCode;
import com.jos.dem.vetlog.model.User;
import com.jos.dem.vetlog.repository.RegistrationCodeRepository;
import com.jos.dem.vetlog.repository.UserRepository;
import com.jos.dem.vetlog.service.impl.RecoveryServiceImpl;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.TestPropertySource;

@Slf4j
@TestPropertySource(properties = {"template.register.name=registerTemplate"})
class RecoveryServiceTest {

    private static final String TOKEN = "token";
    private static final String EMAIL = "contact@josdem.io";
    private RecoveryService service;

    private User user = new User();

    @Mock
    private RestService restService;

    @Mock
    private RegistrationService registrationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RegistrationCodeRepository repository;

    @Mock
    private LocaleService localeService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        service = new RecoveryServiceImpl(restService, registrationService, userRepository, repository, localeService);
    }

    @Test
    @DisplayName("sending activation account token")
    void shouldSendActivationAccountToken(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());

        when(registrationService.findEmailByToken(TOKEN)).thenReturn(Optional.of(EMAIL));
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.ofNullable(user));

        service.confirmAccountForToken(TOKEN);

        assertTrue(user.getEnabled());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("not sending activation account token due to token do not exist")
    void shouldNotSendActivationAccountTokenDueNotValidToken(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        assertThrows(VetlogException.class, () -> service.confirmAccountForToken(TOKEN));
    }

    @Test
    @DisplayName("not sending activation account token due to user not found")
    void shouldNotSendActivationAccountTokenDueUserNotFound(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        when(registrationService.findEmailByToken(TOKEN)).thenReturn(Optional.of(EMAIL));
        assertThrows(UserNotFoundException.class, () -> service.confirmAccountForToken(TOKEN));
    }

    @Test
    @DisplayName("generating token to change password")
    void shouldSendChangePasswordToken(TestInfo testInfo) throws IOException {
        log.info("Running: {}", testInfo.getDisplayName());

        user.setEnabled(true);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.ofNullable(user));
        when(registrationService.generateToken(EMAIL)).thenReturn(TOKEN);

        service.generateRegistrationCodeForEmail(EMAIL);
        verify(restService).sendMessage(isA(MessageCommand.class));
    }

    @Test
    @DisplayName("not sending change password token due to user not found")
    void shouldNotSendChangePasswordTokenDueUserNotFound(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        assertThrows(UserNotFoundException.class, () -> service.generateRegistrationCodeForEmail(EMAIL));
    }

    @Test
    @DisplayName("not sending change password token due to user not enabled")
    void shouldNotSendChangePasswordTokenDueUserNotEnabled(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        user.setEnabled(false);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.ofNullable(user));
        assertThrows(VetlogException.class, () -> service.generateRegistrationCodeForEmail(EMAIL));
    }

    @Test
    @DisplayName("validating a token")
    void shouldValidateToken(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        when(repository.findByToken(TOKEN)).thenReturn(Optional.of(new RegistrationCode()));
        assertTrue(service.validateToken(TOKEN));
    }

    @Test
    @DisplayName("changing password")
    void shouldChangePassword(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        ChangePasswordCommand changePasswordCommand = new ChangePasswordCommand();
        changePasswordCommand.setToken(TOKEN);
        changePasswordCommand.setPassword("password");
        when(registrationService.findEmailByToken(TOKEN)).thenReturn(Optional.of(EMAIL));
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.ofNullable(user));

        service.changePassword(changePasswordCommand);

        assertEquals(60, user.getPassword().length());
        verify(userRepository).save(user);
    }
}
