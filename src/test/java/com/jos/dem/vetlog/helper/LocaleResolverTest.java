package com.jos.dem.vetlog.helper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
class LocaleResolverTest {

  private LocaleResolver localeResolver = new LocaleResolver();

  @Test
  @DisplayName("getting default locale")
  void shouldGetDefaultLocale(TestInfo testInfo) {
    log.info("Running: {}", testInfo.getDisplayName());
    HttpServletRequest request = mock(HttpServletRequest.class);
    Locale result = localeResolver.resolveLocale(request);
    assertEquals(new Locale("en"), result);
  }

  @Test
  @DisplayName("getting english from headers")
  void shouldGetEnglishFromHeaders(TestInfo testInfo){
    log.info("Running: {}", testInfo.getDisplayName());
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getHeader("Accept-Language")).thenReturn("en-US,en;q=0.8");
    Locale result = localeResolver.resolveLocale(request);
    assertEquals(new Locale("en"), result);
  }
}
