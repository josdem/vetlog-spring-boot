package com.jos.dem.vetlog.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
class DateFormatterTest {

    private final DateFormatter dateFormatter = new DateFormatter();

    @Test
    @DisplayName("Formatting a date")
    void shouldFormatADate() {
        // given
        var date = LocalDateTime.parse("2021-11-17T10:15:00");

        // when
        var formattedDate = dateFormatter.format(date);

        // then
        assertEquals("11/17/2021", formattedDate);
    }

    @Test
    @DisplayName("Formatting old date")
    void shouldFormatOldDate() {
        // given
        var date = LocalDateTime.parse("1999-08-18T10:14:00");

        // when
        var formattedDate = dateFormatter.format(date);

        // then
        assertEquals("08/18/1999", formattedDate);
    }
}
