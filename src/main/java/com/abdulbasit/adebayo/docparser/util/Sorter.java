package com.abdulbasit.adebayo.docparser.util;

import com.abdulbasit.adebayo.docparser.model.CarBrand;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Sorter {

    public static List<CarBrand> sort(List<CarBrand> cars, Map<String, String> sortConfig) {
        if (sortConfig == null || sortConfig.isEmpty()) {
            return cars;
        }

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
