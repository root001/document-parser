package com.abdulbasit.adebayo.docparser.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Component
public class ModelLookup {
    private static final Logger logger = LoggerFactory.getLogger(ModelLookup.class);
    private static final String CSV_DELIMITER = ",";
    private static final String UNKNOWN_BRAND = "Unknown";
    
    private static final String UNKNOWN_BRAND = "Unknown";

    private final Map<String, String> modelToBrandMap;
    private final boolean caseSensitive;

    @Autowired
    public ModelLookup(@Value("${model.lookup.path}") String lookupFilePath) throws IOException {
        this(lookupFilePath, false);
    }

    public ModelLookup(@Value("${model.lookup.path}") String lookupFilePath, boolean caseSensitive) throws IOException {
        this.modelToBrandMap = new HashMap<>();
        this.caseSensitive = caseSensitive;
        loadLookupData(Paths.get(lookupFilePath));
    }

    private void loadLookupData(Path lookupFilePath) throws IOException {
        if (lookupFilePath == null) {
            throw new IllegalArgumentException("Lookup file path cannot be null");
        }
        if (!Files.exists(lookupFilePath)) {
            throw new IOException("Lookup file does not exist: " + lookupFilePath);
        }

        try (BufferedReader reader = Files.newBufferedReader(lookupFilePath)) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(CSV_DELIMITER, -1);
                if (parts.length < 2) {
                    logger.warn("Invalid mapping at line {}: {}", lineNumber, line);
                    continue;
                }

                String model = parts[0].trim();
                String brand = parts[1].trim();

                if (model.isEmpty() || brand.isEmpty()) {
                    logger.warn("Empty model or brand at line {}: {}", lineNumber, line);
                    continue;
                }

                modelToBrandMap.put(
                        caseSensitive ? model : model.toLowerCase(),
                        brand
                );
            }
        }
        logger.info("Loaded {} valid model-brand mappings", modelToBrandMap.size());
    }

    public String getBrandForModel(String model) {
        if (model == null || model.isBlank()) {
            logger.debug("Null or blank model provided");
            return UNKNOWN_BRAND;
        }

        String lookupKey = caseSensitive ? model : model.toLowerCase();
        String brand = modelToBrandMap.get(lookupKey);

        if (brand == null) {
            logger.debug("No brand found for model: {}", model);
            return UNKNOWN_BRAND;
        }
        return brand;
    }
}
