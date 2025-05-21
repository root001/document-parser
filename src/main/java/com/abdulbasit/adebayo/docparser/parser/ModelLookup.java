package com.abdulbasit.adebayo.docparser.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ModelLookup {
    private static final Logger logger = LoggerFactory.getLogger(ModelLookup.class);
    private static final String CSV_DELIMITER = ",";
    private static final String UNKNOWN_BRAND = "Unknown";
    
    private final Map<String, String> modelToBrandMap;

    public ModelLookup(Path lookupFilePath) throws IOException {
        this.modelToBrandMap = new HashMap<>();
        loadLookupData(lookupFilePath);
    }

    private void loadLookupData(Path lookupFilePath) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(lookupFilePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split(CSV_DELIMITER);
                if (parts.length >= 2) {
                    modelToBrandMap.put(parts[0].trim(), parts[1].trim());
                }
            }
        }
        logger.info("Loaded {} model-brand mappings", modelToBrandMap.size());
    }

    public String getBrandForModel(String model) {
        String brand = modelToBrandMap.get(model);
        if (brand == null) {
            logger.debug("No brand found for model: {}", model);
            return UNKNOWN_BRAND;
        }
        return brand;
    }
}
