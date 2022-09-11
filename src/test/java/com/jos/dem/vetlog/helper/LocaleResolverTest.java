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
  private HttpServletRequest request = mock(HttpServletRequest.class);

  @Test
  @DisplayName("getting default locale")
  void shouldGetDefaultLocale(TestInfo testInfo) {
    log.info("Running: {}", testInfo.getDisplayName());
    Locale result = localeResolver.resolveLocale(request);
    assertEquals(new Locale("en"), result);
  }

  @Test
  @DisplayName("getting english from headers")
  void shouldGetEnglishFromHeaders(TestInfo testInfo){
    log.info("Running: {}", testInfo.getDisplayName());
    when(request.getHeader("Accept-Language")).thenReturn("en-US,en;q=0.8");
    Locale result = localeResolver.resolveLocale(request);
    assertEquals(new Locale("en"), result);
  }

  @Test
  @DisplayName("getting spanish from headers")
  void shouldGetSpanishFromHeaders(TestInfo testInfo){
    log.info("Running: {}", testInfo.getDisplayName());
    when(request.getHeader("Accept-Language")).thenReturn("es-MX,en-US;q=0.7,en;q=0.3");
    Locale result = localeResolver.resolveLocale(request);
    assertEquals(new Locale("es"), result);
  }
}
