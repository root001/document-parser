package com.abdulbasit.adebayo.docparser.util;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class DateFormatterTest {
    @Test
    void formatForOutput_ValidDate_ReturnsFormattedString() {
        LocalDate date = LocalDate.of(2023, 1, 15);
        assertEquals("2023,15,01", DateFormatter.formatForOutput(date));
    }

    @Test
    void formatForOutput_SingleDigitValues_ReturnsPaddedString() {
        LocalDate date = LocalDate.of(2023, 3, 5);
        assertEquals("2023,05,03", DateFormatter.formatForOutput(date));
    }

    @Test
    void formatForOutput_NullDate_ReturnsEmptyString() {
        assertEquals("", DateFormatter.formatForOutput(null));
    }

    @Test
    void parseFromInput_ValidString_ReturnsLocalDate() {
        LocalDate date = DateFormatter.parseFromInput("01/15/2023");
        assertEquals(LocalDate.of(2023, 1, 15), date);
    }

    @Test
    void parseFromInput_InvalidString_ThrowsException() {
        assertThrows(Exception.class, () -> DateFormatter.parseFromInput("invalid-date"));
    }

    @Test
    void parseFromInput_NullOrBlank_ReturnsNull() {
        assertNull(DateFormatter.parseFromInput(null));
        assertNull(DateFormatter.parseFromInput(""));
    }
}
