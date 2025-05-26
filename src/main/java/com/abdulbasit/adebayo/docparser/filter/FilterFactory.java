package com.abdulbasit.adebayo.docparser.filter;

import com.abdulbasit.adebayo.docparser.model.CarBrand;
import com.abdulbasit.adebayo.docparser.util.DateFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class FilterFactory {
    private static final Logger logger = LoggerFactory.getLogger(FilterFactory.class);

    public static Predicate<CarBrand> createFilter(Map<String, Object> filterConfig) {
        if (filterConfig == null || filterConfig.isEmpty()) {
            return car -> true; // No filtering
        }

        // Flatten nested configurations
        Map<String, Object> flatConfig = flattenFilterConfig(filterConfig);

        Predicate<CarBrand> combinedFilter = car -> true;

        // Brand filter
        if (flatConfig.containsKey("brand")) {
            String brand = (String) flatConfig.get("brand");
            combinedFilter = combinedFilter.and(
                    car -> car.productName().equalsIgnoreCase(brand));
        }

        // Price range filter
        if (flatConfig.containsKey("minPrice") &&
                flatConfig.containsKey("maxPrice")) {
            double minPrice = Double.parseDouble(flatConfig.get("minPrice").toString());
            double maxPrice = Double.parseDouble(flatConfig.get("maxPrice").toString());
            combinedFilter = combinedFilter.and(
                    car -> car.price() != null &&
                            car.price().amount() >= minPrice &&
                            car.price().amount() <= maxPrice);
        }

        // Date range filter
        if (flatConfig.containsKey("startDate") &&
                flatConfig.containsKey("endDate")) {
            LocalDate startDate = DateFormatter.parseFromInput(
                    (String) flatConfig.get("startDate"));
            LocalDate endDate = DateFormatter.parseFromInput(
                    (String) flatConfig.get("endDate"));
            combinedFilter = combinedFilter.and(
                    car -> car.releaseDate() != null &&
                            !car.releaseDate().isBefore(startDate) &&
                            !car.releaseDate().isAfter(endDate));
        }

        // Currency filter
        if (flatConfig.containsKey("currency")) {
            String currency = (String) flatConfig.get("currency");
            combinedFilter = combinedFilter.and(
                    car -> car.price() != null &&
                            car.price().currency().equalsIgnoreCase(currency));
        }

        logger.debug("Final combined filter to apply: {}", buildFilterDescription(flatConfig));
        return combinedFilter;
    }

    private static Map<String, Object> flattenFilterConfig(Map<String, Object> filterConfig) {
        Map<String, Object> flattened = new HashMap<>();

        for (Map.Entry<String, Object> entry : filterConfig.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Map) {
                // If the value is a nested map, add its contents to the flattened map
                Map<String, Object> nestedMap = (Map<String, Object>) value;
                flattened.putAll(nestedMap);
                logger.debug("Flattened nested config '{}': {}", entry.getKey(), nestedMap);
            } else {
                // If it's a direct value, add it as is
                flattened.put(entry.getKey(), value);
            }
        }

        return flattened;
    }

    public static String buildFilterDescription(Map<String, Object> filterConfig) {
        if (filterConfig == null || filterConfig.isEmpty()) {
            return "No filters";
        }

        // Flatten the config first
        Map<String, Object> flatConfig = flattenFilterConfig(filterConfig);

        StringBuilder sb = new StringBuilder();
        if (flatConfig.containsKey("brand")) {
            sb.append("Brand=").append(flatConfig.get("brand")).append(" ");
        }
        if (flatConfig.containsKey("minPrice") && flatConfig.containsKey("maxPrice")) {
            sb.append(String.format("Price[%s-%s] ",
                    flatConfig.get("minPrice"),
                    flatConfig.get("maxPrice")));
        }
        if (flatConfig.containsKey("startDate") && flatConfig.containsKey("endDate")) {
            sb.append(String.format("Date[%s-%s] ",
                    flatConfig.get("startDate"),
                    flatConfig.get("endDate")));
        }
        if (flatConfig.containsKey("currency")) {
            sb.append("Currency=").append(flatConfig.get("currency")).append(" ");
        }
        return sb.toString().trim();
    }
}