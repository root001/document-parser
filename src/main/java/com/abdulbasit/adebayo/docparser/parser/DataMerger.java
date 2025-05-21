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

    public List<BrandRelease> mergeData(List<BrandRelease> csvData, List<BrandRelease> xmlData) {
        // Create lookup map: productName -> releaseDate from CSV
        Map<String, LocalDate> productDateMap = new HashMap<>();
        csvData.forEach(release -> 
            productDateMap.put(release.productName(), release.releaseDate())
        );

        // Merge XML data with CSV dates
        List<BrandRelease> mergedData = xmlData.stream()
            .map(xmlRelease -> {
                LocalDate csvDate = productDateMap.get(xmlRelease.productName());
                return new BrandRelease(
                    xmlRelease.brandName(),
                    xmlRelease.productName(),
                    csvDate != null ? csvDate : xmlRelease.releaseDate(),
                    xmlRelease.version()
                );
            })
            .collect(Collectors.toList());

        logger.info("Merged {} XML records with CSV dates", mergedData.size());
        return mergedData;
    }
}
