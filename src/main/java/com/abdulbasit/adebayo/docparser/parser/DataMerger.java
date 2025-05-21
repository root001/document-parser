package com.abdulbasit.adebayo.docparser.parser;

import com.abdulbasit.adebayo.docparser.model.BrandRelease;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataMerger {
    private static final Logger logger = LoggerFactory.getLogger(DataMerger.class);

    public List<Car> mergeData(List<BrandRelease> csvData, List<BrandRelease> xmlData, Predicate<Car> filter) {
        // Create lookup map: productName -> releaseDate from CSV
        Map<String, LocalDate> productDateMap = new HashMap<>();
        csvData.forEach(release -> 
            productDateMap.put(release.productName(), release.releaseDate())
        );

        // Merge XML data with CSV dates
        List<Car> mergedData = xmlData.stream()
            .map(xmlRelease -> {
                LocalDate csvDate = productDateMap.get(xmlRelease.productName());
                return new Car(
                    xmlRelease.brandName(),
                    xmlRelease.productName(),
                    Double.parseDouble(xmlRelease.version().substring(1)),
                    csvDate != null ? csvDate : xmlRelease.releaseDate()
                );
            })
            .collect(Collectors.toList());

        // Apply filter if provided
        List<BrandRelease> filteredData = filter != null ? 
            mergedData.stream()
                .filter(release -> {
                    Car car = new Car(
                        release.brandName(),
                        release.productName(),
                        // Convert version to price for demo purposes
                        Double.parseDouble(release.version().substring(1))
                    );
                    return filter.test(car);
                })
                .collect(Collectors.toList()) :
            mergedData;

        logger.info("Filtered {} records down to {}", mergedData.size(), filteredData.size());
        return filteredData;
    }
}
