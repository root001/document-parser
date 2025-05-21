package com.abdulbasit.adebayo.docparser.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateFormatter {
    private static final DateTimeFormatter OUTPUT_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy,dd,MM");
    private static final DateTimeFormatter INPUT_FORMATTER = 
        DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public static String formatForOutput(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(OUTPUT_FORMATTER);
    }

    public static LocalDate parseFromInput(String dateString) {
        if (dateString == null || dateString.isBlank()) {
            return null;
        }
        return LocalDate.parse(dateString, INPUT_FORMATTER);
    }
}
