package com.abdulbasit.adebayo.docparser.parser;

import com.abdulbasit.adebayo.docparser.model.Brand;
import com.abdulbasit.adebayo.docparser.model.CarBrand;
import com.abdulbasit.adebayo.docparser.exception.ParseException;
import com.abdulbasit.adebayo.docparser.util.DateFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.IOException;
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
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy/dd/MM");
    private static final String CSV_DELIMITER = ",";

    public List<Brand> parse(Path csvPath) throws ParseException, IOException {
        logger.info("Starting CSV parsing for file: {}", csvPath);
        List<Brand> releases = new ArrayList<>();
        
        try (BufferedReader reader = Files.newBufferedReader(csvPath)) {
            logger.debug("Opened CSV file successfully");
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
                    
                    LocalDate releaseDate = DateFormatter.parseFromInput(parts[2].trim());
                    Brand release = new Brand(
                        parts[0].trim(),
                        releaseDate
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
