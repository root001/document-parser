package com.abdulbasit.adebayo.docparser.parser;

import com.abdulbasit.adebayo.docparser.model.BrandRelease;
import com.abdulbasit.adebayo.docparser.exception.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CsvParser {
    private static final Logger logger = LoggerFactory.getLogger(CsvParser.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final String CSV_DELIMITER = ",";

    public List<BrandRelease> parse(Path csvPath) throws ParseException, IOException {
        List<BrandRelease> releases = new ArrayList<>();
        
        try (BufferedReader reader = Files.newBufferedReader(csvPath)) {
            String line;
            int lineNumber = 0;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;
                
                try {
                    String[] parts = line.split(CSV_DELIMITER);
                    if (parts.length != 4) {
                        throw new ParseException("Invalid CSV format at line " + lineNumber);
                    }
                    
                    LocalDate releaseDate = parseDate(parts[2].trim());
                    BrandRelease release = new BrandRelease(
                        parts[0].trim(),
                        parts[1].trim(),
                        releaseDate,
                        parts[3].trim()
                    );
                    releases.add(release);
                } catch (IllegalArgumentException | DateTimeParseException e) {
                    logger.warn("Skipping invalid row at line {}: {}", lineNumber, e.getMessage());
                    throw new ParseException("Failed to parse line " + lineNumber + ": " + e.getMessage());
                }
            }
        }
        
        logger.info("Successfully parsed {} brand releases", releases.size());
        return releases;
    }

    private LocalDate parseDate(String dateStr) throws DateTimeParseException {
        return LocalDate.parse(dateStr, DATE_FORMATTER);
    }
}
