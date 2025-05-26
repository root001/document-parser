package com.abdulbasit.adebayo.docparser.util;

import com.abdulbasit.adebayo.docparser.model.CarBrand;
import com.abdulbasit.adebayo.docparser.model.Price;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class Sorter {

    private static final Logger logger = LoggerFactory.getLogger(Sorter.class);

    public static List<CarBrand> sort(List<CarBrand> cars, Map<String, String> sortConfig) {
        return sort(cars, sortConfig, null);
    }

    public static List<CarBrand> sort(List<CarBrand> cars, Map<String, String> sortConfig, Map<String, String> currencyMapping) {
        if (cars == null || cars.isEmpty()) {
            return cars;
        }

        // Check if grouped currency sorting is requested
        if (currencyMapping != null && !currencyMapping.isEmpty()) {
            logger.info("Applying grouped currency-type sorting: {}", currencyMapping);
            return sortByCurrencyAndType(cars, currencyMapping);
        }

        // Regular sorting logic (existing)
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

    private static List<CarBrand> sortByCurrencyAndType(List<CarBrand> cars, Map<String, String> currencyMapping) {
        List<CarBrand> result = new ArrayList<>();
        Set<String> processedCarNames = new HashSet<>();

        // Process each currency-type combination in the order provided
        for (Map.Entry<String, String> entry : currencyMapping.entrySet()) {
            String currency = entry.getKey();
            String vehicleType = entry.getValue();

            List<CarBrand> group = cars.stream()
                    .filter(car -> !processedCarNames.contains(car.model())) // Skip already processed cars
                    .filter(car -> matchesCurrencyAndType(car, currency, vehicleType))
                    .map(car -> updateCarPriceToCurrency(car, currency))
                    .sorted(Comparator.comparing((CarBrand car) -> car.price() != null ? car.price().amount() : 0.0).reversed())
                    .collect(Collectors.toList());

            if (!group.isEmpty()) {
                logger.debug("Found {} cars for {} {}", group.size(), currency, vehicleType);
                result.addAll(group);
                // Mark these cars as processed
                group.forEach(car -> processedCarNames.add(car.model()));
            }
        }
        logger.debug("Processed cars with name : {}", processedCarNames);

        // Handle remaining cars in USD (default) - cars that didn't match any specific combination
        List<CarBrand> remainingCars = cars.stream()
                .filter(car -> !processedCarNames.contains(car.model()))
                .filter(car -> !car.priceList().isEmpty())
                .sorted(Comparator.comparing((CarBrand car) -> car.price() != null ? car.price().amount() : 0.0).reversed())
                .collect(Collectors.toList());

        if (!remainingCars.isEmpty()) {
            logger.debug("Found {} remaining cars in USD (default)", remainingCars.size());
            result.addAll(remainingCars);
        }

        logger.info("Total cars after grouped sorting: {} (from {} original)", result.size(), cars.size());
        return result;
    }

    private static CarBrand updateCarPriceToCurrency(CarBrand car, String targetCurrency) {
        // Find the price in the target currency from the price list
        Optional<Price> targetPrice = car.priceList().stream()
                .filter(price -> targetCurrency.equalsIgnoreCase(price.currency()))
                .findFirst();

        if (targetPrice.isPresent()) {
            // Create a new CarBrand with the updated main price
            return new CarBrand(
                    car.model(),
                    car.brandType(),
                    car.model(),
                    car.releaseDate(),
                    targetPrice.get(), // Update the main price to the target currency
                    car.priceList() // Keep the original price list unchanged
            );
        } else {
            // If target currency not found, return the original car
            logger.warn("Target currency {} not found for car {}, keeping original price",
                    targetCurrency, car.model());
            return car;
        }
    }

    private static boolean matchesCurrencyAndType(CarBrand car, String currency, String vehicleType) {
        if (car.priceList().isEmpty() || car.brandType() == null) {
            return false;
        }

        // Check if the car's type matches the vehicle type
        boolean typeMatches = vehicleType.equalsIgnoreCase(car.brandType());

        // Check if the car has a price in the specified currency
        boolean currencyExists = car.priceList().stream()
                .anyMatch(price -> currency.equalsIgnoreCase(price.currency()));

        return typeMatches && currencyExists;
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

    // Utility method to build description of sorting applied
    public static String buildSortDescription(Map<String, String> sortConfig, Map<String, String> currencyMapping) {
        if (currencyMapping != null && !currencyMapping.isEmpty()) {
            StringBuilder sb = new StringBuilder("Grouped by currency-type: ");
            currencyMapping.entrySet().forEach(entry ->
                    sb.append(String.format("%s %s, ", entry.getKey(), entry.getValue())));
            sb.append("then USD default (all by price desc)");
            return sb.toString();
        } else if (sortConfig != null && !sortConfig.isEmpty()) {
            return String.format("Regular sort by %s %s",
                    sortConfig.get("by"), sortConfig.get("order"));
        }
        return "No sorting applied";
    }

    // Helper method to get price for a specific currency (kept for potential future use)
    private static double getPriceForCurrency(CarBrand car, String currency) {
        return car.priceList().stream()
                .filter(price -> currency.equalsIgnoreCase(price.currency()))
                .mapToDouble(price -> price.amount())
                .findFirst()
                .orElse(0.0);
    }
}