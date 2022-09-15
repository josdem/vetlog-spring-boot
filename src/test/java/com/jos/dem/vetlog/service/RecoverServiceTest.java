package com.jos.dem.vetlog.service;

import com.jos.dem.vetlog.command.Command;
import com.jos.dem.vetlog.command.MessageCommand;
import com.jos.dem.vetlog.model.User;
import com.jos.dem.vetlog.repository.RegistrationCodeRepository;
import com.jos.dem.vetlog.repository.UserRepository;
import com.jos.dem.vetlog.service.impl.RecoveryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@Slf4j
@TestPropertySource(properties = {"template.register.name=registerTemplate"})
class RecoverServiceTest {

    private RecoveryService service;

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
    @DisplayName("send confirmation token")
    void shouldSendConfirmationToken(TestInfo testInfo) throws IOException {
        log.info("Running: {}", testInfo.getDisplayName());

        service.sendConfirmationAccountToken("contact@josdem.io");

        verify(registrationService).generateToken(anyString());
        verify(restService).sendMessage(isA(MessageCommand.class));
    }

    @Test
    @DisplayName("sending activation account token")
    void shouldSendActivationAccountToken(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());

        when(registrationService.findEmailByToken("token")).thenReturn("contact@josdem.io");
        User user = new User();
        when(userRepository.findByEmail("contact@josdem.io")).thenReturn(user);

        service.confirmAccountForToken("token");

        assertTrue(user.getEnabled());
        verify(userRepository).save(user);
    }
}
