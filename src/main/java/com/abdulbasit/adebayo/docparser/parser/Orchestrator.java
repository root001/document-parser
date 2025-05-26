package com.abdulbasit.adebayo.docparser.parser;

import com.abdulbasit.adebayo.docparser.config.Config;
import com.abdulbasit.adebayo.docparser.config.ConfigLoader;
import com.abdulbasit.adebayo.docparser.exception.ConfigException;
import com.abdulbasit.adebayo.docparser.filter.FilterFactory;
import com.abdulbasit.adebayo.docparser.model.Brand;
import com.abdulbasit.adebayo.docparser.model.Car;
import com.abdulbasit.adebayo.docparser.model.CarBrand;
import com.abdulbasit.adebayo.docparser.util.Constants;
import com.abdulbasit.adebayo.docparser.util.Sorter;
import com.abdulbasit.adebayo.docparser.writer.OutputWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Component
public class Orchestrator {

    private static final Logger logger = LoggerFactory.getLogger(Orchestrator.class);
    private final ConfigLoader configLoader;
    private final CsvParser csvParser;
    private final XmlParser xmlParser;
    private final DataMerger dataMerger;

    public Orchestrator(ConfigLoader configLoader, CsvParser csvParser, XmlParser xmlParser, DataMerger dataMerger) {
        this.configLoader = configLoader;
        this.csvParser = csvParser;
        this.xmlParser = xmlParser;
        this.dataMerger = dataMerger;
    }

    public void process(String configPath) throws IOException {
        Config config = loadConfig(configPath);
        Predicate<CarBrand> filter = createFilter(config);
        List<CarBrand> results = processData(config, filter);
        writeOutput(config, results);
    }

    private Config loadConfig(String configPath) {
        try {
            logger.info("Loading configuration from: {}", configPath);
            return configLoader.loadConfig(configPath);
        } catch (ConfigException e) {
            logger.error("Failed to load configuration: {}", e.getMessage());
            throw e;
        }
    }

    private Predicate<CarBrand> createFilter(Config config) {
        if (config.getFilters() == null || config.getFilters().isEmpty()) {
            logger.info("No filters configured");
            return car -> true;
        }

        logger.info("Creating filter from config: {}", config.getFilters());

        // Flatten nested filter configurations
        Map<String, Object> flattenedFilters = new HashMap<>();
        for (Map.Entry<String, Object> entry : config.getFilters().entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Map) {
                // If the value is a nested map, add its contents to the flattened map
                Map<String, Object> nestedMap = (Map<String, Object>) value;
                flattenedFilters.putAll(nestedMap);
            } else {
                // If it's a direct value, add it as is
                flattenedFilters.put(entry.getKey(), value);
            }
        }

        logger.debug("Flattened filter config: {}", flattenedFilters);
        return FilterFactory.createFilter(flattenedFilters);
    }

    private void writeOutput(Config config, List<CarBrand> results) throws IOException {
        String outputFormat = config.getOutputFormat() != null ? 
                            config.getOutputFormat().toLowerCase() : "json";
        Path outputPath = Paths.get(System.getProperty("user.dir"),
                Constants.DEFAULT_OUTPUT_DIR + LocalTime.now().getNano()
                        + "-"+ config.getOutputPath());
        
        switch (outputFormat) {
            case Constants.OUTPUT_FORMAT_JSON:
                OutputWriter.writeJson(outputPath, results);
                break;
            case Constants.OUTPUT_FORMAT_XML:
                OutputWriter.writeXml(outputPath, results);
                break;
            case Constants.OUTPUT_FORMAT_TABLE:
                OutputWriter.writeTable(outputPath, results);
                break;
            default:
                throw new IllegalArgumentException("Unsupported output format: " + outputFormat);
        }
        
        logger.info("Output written to {} in {} format", outputPath, outputFormat);
    }

    private List<CarBrand> processData(Config config, Predicate<CarBrand> filter) {
        try {
            Path csvPath = Paths.get(config.getInputCsv());
            Path xmlPath = Paths.get(config.getInputXml());

            logger.info("Parsing CSV data from: {}", csvPath);
            List<Brand> csvData = csvParser.parse(csvPath);

            logger.info("Parsing XML data from: {}", xmlPath);
            List<Car> xmlData = xmlParser.parse(xmlPath);

            logger.info("Merging data...");
            List<CarBrand> mergedData = dataMerger.mergeData(csvData, xmlData, filter);

            logger.info("Applying sorting...");
            // Pass both sort config and currency mapping to the enhanced Sorter
            List<CarBrand> sortedData = Sorter.sort(mergedData, config.getSort(), config.getCurrencyMapping());

            logger.info("Sorting applied: {}",
                    Sorter.buildSortDescription(config.getSort(), config.getCurrencyMapping()));

            return sortedData;

        } catch (Exception e) {
            logger.error("Error processing data: {}", e.getMessage());
            throw new RuntimeException("Error processing data", e);
        }
    }
}
