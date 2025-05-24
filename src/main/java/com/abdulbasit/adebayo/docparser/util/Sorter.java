package com.abdulbasit.adebayo.docparser.util;

import com.abdulbasit.adebayo.docparser.model.CarBrand;
import com.abdulbasit.adebayo.docparser.parser.Orchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Sorter {

    private static final Logger logger = LoggerFactory.getLogger(Sorter.class);

    public static List<CarBrand> sort(List<CarBrand> cars, Map<String, String> sortConfig) {
        if (sortConfig == null || sortConfig.isEmpty()) {
            return cars;
        }
        logger.debug("Sorting config to apply : {}", sortConfig);

        String sortBy = sortConfig.get("by");
        String sortOrder = sortConfig.get("order");

        if (sortBy == null) {
            return cars;
        }

        Comparator<CarBrand> comparator = getComparator(sortBy);
        if (comparator == null) {
            return cars;
        }

        if (Constants.SORT_DESC.equalsIgnoreCase(sortOrder)) {
            comparator = comparator.reversed();
        }

        return cars.stream()
                .sorted(comparator)
                .toList();
    }

    private static String buildFilterDescription(Map<String, Object> filterConfig) {
        if (filterConfig == null || filterConfig.isEmpty()) {
            return "No filters";
        }
        
        StringBuilder sb = new StringBuilder();
        if (filterConfig.containsKey("brand")) {
            sb.append("Brand=").append(filterConfig.get("brand")).append(" ");
        }
        if (filterConfig.containsKey("minPrice") && filterConfig.containsKey("maxPrice")) {
            sb.append(String.format("Price[%s-%s] ", 
                filterConfig.get("minPrice"), 
                filterConfig.get("maxPrice")));
        }
        if (filterConfig.containsKey("startDate") && filterConfig.containsKey("endDate")) {
            sb.append(String.format("Date[%s-%s] ", 
                filterConfig.get("startDate"), 
                filterConfig.get("endDate")));
        }
        if (filterConfig.containsKey("currency")) {
            sb.append("Currency=").append(filterConfig.get("currency")).append(" ");
        }
        return sb.toString().trim();
    }

    private static Comparator<CarBrand> getComparator(String sortBy) {
        switch (sortBy.toLowerCase()) {
            case Constants.SORT_BY_DATE:
                return Comparator.comparing(CarBrand::releaseDate);
            case Constants.SORT_BY_PRICE:
                return Comparator.comparing(car -> 
                    car.price() != null ? car.price().amount() : 0.0);
            default:
                return null;
        }
    }
}
