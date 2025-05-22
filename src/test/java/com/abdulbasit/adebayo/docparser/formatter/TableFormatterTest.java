package com.abdulbasit.adebayo.docparser.formatter;

import com.abdulbasit.adebayo.docparser.model.CarBrand;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TableFormatterTest {
    @Test
    void format_SingleRelease_ReturnsFormattedTable() {
        List<CarBrand> releases = List.of(
            new CarBrand("Toyota", "RAV4", LocalDate.of(2023, 1, 15), "v1.0")
        );
        
        TableFormatter formatter = new TableFormatter();
        String result = formatter.format(releases);
        
        assertTrue(result.contains("| Brand     | Model     | Release Date (yyyy,dd,MM) | Version |"));
        assertTrue(result.contains("| Toyota    | RAV4      | 2023,15,01                | v1.0    |"));
    }

    @Test
    void format_LongValues_TruncatesWithEllipsis() {
        List<CarBrand> releases = List.of(
            new CarBrand("Toyota Motor Corporation", "RAV4 Hybrid XSE", LocalDate.of(2023, 1, 15), "version1.0")
        );
        
        TableFormatter formatter = new TableFormatter();
        String result = formatter.format(releases);
        
        assertTrue(result.contains("| Toyota…   | RAV4 Hyb… | 2023,15,01                | versi…  |"));
    }

    @Test
    void format_EmptyList_ReturnsHeaderOnly() {
        TableFormatter formatter = new TableFormatter();
        String result = formatter.format(List.of());
        
        assertTrue(result.contains("| Brand     | Model     | Release Date (yyyy,dd,MM) | Version |"));
        assertFalse(result.contains("2023,15,01")); // No data rows
    }
}
