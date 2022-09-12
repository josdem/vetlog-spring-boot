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
    assertEquals("01/17/2021", dateFormatter.format("1/17/21, 12:00 AM"));
  }
}
