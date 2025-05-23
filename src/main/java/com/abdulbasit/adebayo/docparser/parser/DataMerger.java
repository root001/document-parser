package com.abdulbasit.adebayo.docparser.parser;

import com.abdulbasit.adebayo.docparser.model.Brand;
import com.abdulbasit.adebayo.docparser.model.CarBrand;
import com.abdulbasit.adebayo.docparser.model.Car;
import com.abdulbasit.adebayo.docparser.model.Price;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class DataMerger {
    private static final Logger logger = LoggerFactory.getLogger(DataMerger.class);

    @Value("${car.brand.mapping}")
    private String carBrandMappingConfig2;

    private String carBrandMappingConfig = "Toyota=RAV4,Honda=Civic,Ford=F-150,Tesla=Model X,BMW=330i,Audi=Q5,Chevrolet=Silverado,Mercedes-Benz=C-Class,Nissan=Rogue,Hyundai=Elantra";

    private Map<String, String> carBrandMap;

    // Initialize the brand mapping when the class is instantiated
    public DataMerger() {
        // This will be called after Spring sets the @Value, but we'll also call it in mergeData as a fallback
    }

    private void ensureBrandMappingInitialized() {
        if (carBrandMap == null) {
            logger.info("--- Static hardcoded test : {}", carBrandMappingConfig);
            logger.info("--- +++ Mapping from config file : {}", carBrandMappingConfig2);
            this.carBrandMap = parseCarBrandMapping(carBrandMappingConfig);
            logger.info("Initialized car brand mapping with {} entries", carBrandMap.size());
        }
    }

    /**
     * Merges CSV brand data with XML car data to create a unified list of CarBrand objects.
     * The merge is performed using the carBrandMap to match Brand.productName() with Car.model().
     * Records without matching mapping or release dates are excluded from the result.
     *
     * @param csvData List of Brand objects from CSV file containing productName and releaseDate
     * @param xmlData List of Car objects from XML file containing type, model, price, and priceList
     * @param filter Optional predicate to filter the merged results
     * @return List of merged and filtered CarBrand objects
     */
    public List<CarBrand> mergeData(List<Brand> csvData, List<Car> xmlData, Predicate<CarBrand> filter) {
        logger.info("Starting data merge with {} CSV records and {} XML records", 
            csvData != null ? csvData.size() : 0,
            xmlData != null ? xmlData.size() : 0);
            
        // Ensure brand mapping is initialized
        ensureBrandMappingInitialized();

        if (csvData == null || xmlData == null) {
            logger.warn("Null data provided - csvData: {}, xmlData: {}", csvData != null, xmlData != null);
            return Collections.emptyList();
        }

        // Create lookup map: productName -> releaseDate from CSV
        Map<String, LocalDate> productDateMap = createProductDateMap(csvData);

        // Merge XML car data with CSV brand data using brand mapping
        List<CarBrand> mergedData = mergeXmlWithCsvData(xmlData, productDateMap);

        // Apply filter if provided
        List<CarBrand> filteredData = applyFilter(mergedData, filter);

        logger.info("Merged {} XML records with {} CSV records, filtered to {} results",
                xmlData.size(), csvData.size(), filteredData.size());

        return filteredData;
    }

    /**
     * Creates a map of brand names to release dates from CSV data.
     * Note: Assuming the CSV Brand field represents the brand name (productName in Brand record).
     */
    private Map<String, LocalDate> createProductDateMap(List<Brand> csvData) {
        return csvData.stream()
                .filter(Objects::nonNull)
                .filter(brand -> brand.productName() != null && brand.releaseDate() != null)
                .collect(Collectors.toMap(
                        Brand::productName,
                        Brand::releaseDate,
                        (existing, replacement) -> {
                            logger.warn("Duplicate brand name found: {}. Using existing date.", existing);
                            return existing;
                        }
                ));
    }

    /**
     * Merges XML car data with CSV brand information using the brand mapping.
     * For each Car from XML, finds matching Brand from CSV using the carBrandMap.
     */
    private List<CarBrand> mergeXmlWithCsvData(List<Car> xmlData, Map<String, LocalDate> brandDateMap) {
        logger.info("-- xmlData to merge : {} and mergeXmlWithCsvData dateMap is : {}", xmlData, brandDateMap);
        return xmlData.stream()
                .filter(Objects::nonNull)
                .filter(car -> car.type() != null && car.model() != null)
                .map(car -> createCarBrand(car, brandDateMap) )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Creates a CarBrand object by merging XML Car data with CSV Brand data.
     * Maps Brand.productName() (e.g., "Toyota") to Car.model() (e.g., "RAV4") using carBrandMap.
     */
    private CarBrand createCarBrand(Car xmlCar, Map<String, LocalDate> brandDateMap) {
        try {
            logger.info("Initiating car brand for :{} and dateMap : {}", xmlCar.model(), brandDateMap);
            String carModel = xmlCar.model(); // e.g., "RAV4"

            // Find the brand that corresponds to this car model using reverse lookup
            String brandName = findBrandForModel(carModel);
            logger.info("Found brand name for :{}", brandName);
            if (brandName == null) {
                logger.debug("No brand mapping found for car model: {}. Skipping record.", carModel);
                return null; // Skip records without mapping
            }

            // Look up release date from CSV using the brand name
            LocalDate releaseDate = brandDateMap.get(brandName);
            logger.info("releaseDate for name is :{}", releaseDate);
            // Skip records without matching release date
            if (releaseDate == null) {
                logger.debug("No release date found for brand: {}. Skipping record.", brandName);
                return null;
            }

            return new CarBrand(
                    xmlCar.type(),       // brandType from XML Car.type() (e.g., "SUV")
                    brandName,           // productName from reverse lookup (e.g., "Toyota")
                    xmlCar.model(),      // model from XML (e.g., "RAV4")
                    releaseDate,         // releaseDate from CSV
                    xmlCar.price(),            // price as string
                    xmlCar.priceList()   // priceList from XML
            );
        } catch (Exception e) {
            logger.error("Error creating CarBrand for car model: {}", xmlCar.model(), e);
            return null;
        }
    }

    /**
     * Finds the brand name that corresponds to a given car model using reverse lookup.
     * The carBrandMap is structured as Brand -> Model, so we reverse lookup Model -> Brand.
     */
    private String findBrandForModel(String model) {
        if (model == null) return null;
        
        return carBrandMap.entrySet().stream()
                .filter(entry -> entry.getValue().equalsIgnoreCase(model))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    /**
     * Applies the provided filter to the merged data.
     */
    private List<CarBrand> applyFilter(List<CarBrand> mergedData, Predicate<CarBrand> filter) {
        if (filter == null) {
            return mergedData;
        }

        List<CarBrand> filteredData = mergedData.stream()
                .filter(filter)
                .collect(Collectors.toList());

        logger.debug("Applied filter: {} records filtered to {}", mergedData.size(), filteredData.size());
        return filteredData;
    }

    /**
     * Parses the car brand mapping configuration string.
     * Expected format: "Brand1=Model1,Brand2=Model2,..."
     */
    private Map<String, String> parseCarBrandMapping(String configString) {
        if (configString == null || configString.trim().isEmpty()) {
            logger.warn("No car brand mapping configuration provided");
            return Collections.emptyMap();
        }

        Map<String, String> mapping = new HashMap<>();
        String[] pairs = configString.split(",");

        for (String pair : pairs) {
            String[] keyValue = pair.trim().split("=");
            if (keyValue.length == 2) {
                mapping.put(keyValue[0].trim(), keyValue[1].trim());
            } else {
                logger.warn("Invalid mapping pair: {}", pair);
            }
        }

        return mapping;
    }

    /**
     * Updates the car brand mapping at runtime (useful for testing or dynamic configuration).
     */
    public void updateCarBrandMapping(Map<String, String> newMapping) {
        if (newMapping != null) {
            this.carBrandMap = new HashMap<>(newMapping);
            logger.info("Updated car brand mapping with {} entries", carBrandMap.size());
        }
    }

    /**
     * Gets a copy of the current car brand mapping.
     */
    public Map<String, String> getCarBrandMapping() {
        ensureBrandMappingInitialized();
        return new HashMap<>(carBrandMap);
    }
}
