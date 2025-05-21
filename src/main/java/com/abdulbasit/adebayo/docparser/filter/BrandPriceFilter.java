package com.abdulbasit.adebayo.docparser.filter;

import com.abdulbasit.adebayo.docparser.model.Car;
import java.util.function.Predicate;

public class BrandPriceFilter implements Predicate<Car> {
    private final String brand;
    private final double minPrice;
    private final double maxPrice;

    public BrandPriceFilter(String brand, double minPrice, double maxPrice) {
        this.brand = brand;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    @Override
    public boolean test(Car car) {
        return car.brand().equalsIgnoreCase(brand) 
            && car.price() >= minPrice 
            && car.price() <= maxPrice;
    }
}
