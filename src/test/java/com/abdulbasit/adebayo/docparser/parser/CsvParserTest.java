package com.abdulbasit.adebayo.docparser.parser;

import com.abdulbasit.adebayo.docparser.model.BrandRelease;
import com.abdulbasit.adebayo.docparser.exception.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CsvParserTest {
    private final CsvParser parser = new CsvParser();

    @Test
    void parse_ValidCsv_ReturnsBrandReleases(@TempDir Path tempDir) throws Exception {
        String csvContent = """
            Brand1,Product1,01/15/2023,v1.0
            Brand2,Product2,02/20/2023,v2.0
            """;
        Path csvFile = tempDir.resolve("test.csv");
        Files.writeString(csvFile, csvContent);

        List<BrandRelease> result = parser.parse(csvFile);
        
        assertEquals(2, result.size());
        assertEquals("Brand1", result.get(0).brandName());
        assertEquals(LocalDate.of(2023, 1, 15), result.get(0).releaseDate());
    }

    @Test
    void parse_InvalidDate_ThrowsParseException(@TempDir Path tempDir) throws Exception {
        String csvContent = "Brand1,Product1,13/20/2023,v1.0";
        Path csvFile = tempDir.resolve("test.csv");
        Files.writeString(csvFile, csvContent);

        assertThrows(ParseException.class, () -> parser.parse(csvFile));
    }

    @Test
    void parse_MissingFields_ThrowsParseException(@TempDir Path tempDir) throws Exception {
        String csvContent = "Brand1,Product1";
        Path csvFile = tempDir.resolve("test.csv");
        Files.writeString(csvFile, csvContent);

        assertThrows(ParseException.class, () -> parser.parse(csvFile));
    }
}
