package com.jos.dem.vetlog.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class DateFormatterTest {

  private DateFormatter dateFormatter = new DateFormatter();

  @Test
  @DisplayName("formatting a date")
  void shouldFormatADate(TestInfo testInfo) {
    log.info("Running: {}", testInfo.getDisplayName());
    assertEquals("09/12/2022", dateFormatter.format("2022-09-12T15:34:49.049584795"));
  }
}
