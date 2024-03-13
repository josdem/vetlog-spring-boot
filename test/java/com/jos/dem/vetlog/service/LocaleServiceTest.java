package com.jos.dem.vetlog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.jos.dem.vetlog.helper.LocaleResolver;
import com.jos.dem.vetlog.service.impl.LocaleServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;

@Slf4j
class LocaleServiceTest {

    private LocaleService service;

    @Mock
    private MessageSource messageSource;

    @Mock
    private LocaleResolver localeResolver;

    private String code = "code";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        service = new LocaleServiceImpl(messageSource, localeResolver);
    }

    @Test
    @DisplayName("getting message by request")
    void shouldGetMessageByRequest(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(messageSource.getMessage(code, null, localeResolver.resolveLocale(request)))
                .thenReturn("message");
        assertEquals("message", service.getMessage(code, request));
    }

    @Test
    @DisplayName("getting message by code")
    void shouldGetMessageByCode(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        when(messageSource.getMessage(code, null, new Locale("en"))).thenReturn("message");
        assertEquals("message", service.getMessage(code));
    }
}
