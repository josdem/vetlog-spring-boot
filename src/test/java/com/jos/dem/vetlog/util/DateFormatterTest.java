package com.jos.dem.vetlog.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class DateFormatterTest {

    @Autowired
    private DateFormatter dateFormatter;

    @Test
    @DisplayName("Formatting a date")
    void shouldFormatADate() {
        var date = LocalDateTime.parse("2021-11-17T10:15:00");

        var formattedDate = dateFormatter.formatToDate(date, Locale.ENGLISH);

        assertEquals("11/17/2021", formattedDate);
    }

    @Test
    @DisplayName("Formatting old date")
    void shouldFormatOldDate() {
        var date = LocalDateTime.parse("1999-08-18T10:14:00");

        var formattedDate = dateFormatter.formatToDate(date, Locale.ENGLISH);

        assertEquals("08/18/1999", formattedDate);
    }

    @Test
    @DisplayName("Formatting date for ES locale")
    void shouldFormatDateForEs() {
        var locale = new Locale("es", "ES");
        var date = LocalDateTime.parse("1999-08-18T10:14:00");

        var formattedDate = dateFormatter.formatToDate(date, locale);

        assertEquals("18/08/1999", formattedDate);
    }
}
