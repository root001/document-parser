package com.abdulbasit.adebayo.docparser.parser;

import com.abdulbasit.adebayo.docparser.config.Config;
import com.abdulbasit.adebayo.docparser.config.ConfigLoader;
import com.abdulbasit.adebayo.docparser.exception.ConfigException;
import com.abdulbasit.adebayo.docparser.filter.BrandPriceFilter;
import com.abdulbasit.adebayo.docparser.model.Brand;
import com.abdulbasit.adebayo.docparser.model.Car;
import com.abdulbasit.adebayo.docparser.model.CarBrand;
import com.abdulbasit.adebayo.docparser.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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

    public List<CarBrand> process(String configPath, Double minPrice, Double maxPrice) {
        Config config = loadConfig(configPath);
        Predicate<CarBrand> filter = createFilter(config, minPrice, maxPrice);
        return processData(config, filter);
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

    private Predicate<CarBrand> createFilter(Config config, Double minPrice, Double maxPrice) {
        String brand = (String) config.getFilters().get("brand");
        if (brand != null && minPrice != null && maxPrice != null) {
            logger.info("Creating BrandPriceFilter with brand: {}, minPrice: {}, maxPrice: {}", brand, minPrice, maxPrice);
            return new BrandPriceFilter(brand, minPrice, maxPrice);
        } else {
            logger.info("No BrandPriceFilter will be applied.");
            return null;
        }
    }

    private List<CarBrand> processData(Config config, Predicate<CarBrand> filter) {
        try {
            Path csvPath = Paths.get(config.getInput_csv());
            Path xmlPath = Paths.get(config.getInput_xml());

            logger.info("Parsing CSV data from: {}", csvPath);
            List<Brand> csvData = csvParser.parse(csvPath);

            logger.info("Parsing XML data from: {}", xmlPath);
            List<Car> xmlData = xmlParser.parse(xmlPath);

            logger.info("Merging data...");
            List<CarBrand> mergedData = dataMerger.mergeData(csvData, xmlData, filter);

            return mergedData;

        } catch (Exception e) {
            logger.error("Error processing data: {}", e.getMessage());
            throw new RuntimeException("Error processing data", e);
        }
    }
}
