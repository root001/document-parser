package com.abdulbasit.adebayo.docparser.filter;

import com.abdulbasit.adebayo.docparser.model.CarBrand;
import com.abdulbasit.adebayo.docparser.util.DateFormatter;
import com.abdulbasit.adebayo.docparser.util.Sorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Map;
import java.util.function.Predicate;

public class FilterFactory {
    private static final Logger logger = LoggerFactory.getLogger(FilterFactory.class);
    private static final Logger logger = LoggerFactory.getLogger(FilterFactory.class);

    public static Predicate<CarBrand> createFilter(Map<String, Object> filterConfig) {
        if (filterConfig == null || filterConfig.isEmpty()) {
            return car -> true; // No filtering
        }

        Predicate<CarBrand> combinedFilter = car -> true;
        
        // Brand filter
        if (filterConfig.containsKey("brand")) {
            String brand = (String) filterConfig.get("brand");
            combinedFilter = combinedFilter.and(
                car -> car.productName().equalsIgnoreCase(brand));
        }

        // Price range filter
        if (filterConfig.containsKey("minPrice") && 
            filterConfig.containsKey("maxPrice")) {
            double minPrice = Double.parseDouble(filterConfig.get("minPrice").toString());
            double maxPrice = Double.parseDouble(filterConfig.get("maxPrice").toString());
            combinedFilter = combinedFilter.and(
                car -> car.price() != null && 
                       car.price().amount() >= minPrice && 
                       car.price().amount() <= maxPrice);
        }

        // Date range filter
        if (filterConfig.containsKey("startDate") && 
            filterConfig.containsKey("endDate")) {
            LocalDate startDate = DateFormatter.parseFromInput(
                (String) filterConfig.get("startDate"));
            LocalDate endDate = DateFormatter.parseFromInput(
                (String) filterConfig.get("endDate"));
            combinedFilter = combinedFilter.and(
                car -> car.releaseDate() != null &&
                       !car.releaseDate().isBefore(startDate) &&
                       !car.releaseDate().isAfter(endDate));
        }

        // Currency filter
        if (filterConfig.containsKey("currency")) {
            String currency = (String) filterConfig.get("currency");
            combinedFilter = combinedFilter.and(
                car -> car.price() != null && 
                       car.price().currency().equalsIgnoreCase(currency));
        }

        logger.debug("Final combined filter to apply: {}", combinedFilter);
        return combinedFilter;
    }
}
