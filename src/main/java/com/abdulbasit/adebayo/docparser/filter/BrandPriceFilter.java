package com.abdulbasit.adebayo.docparser.filter;

import com.abdulbasit.adebayo.docparser.model.Car;
import com.abdulbasit.adebayo.docparser.model.CarBrand;

import java.util.function.Predicate;

public class BrandPriceFilter implements Predicate<CarBrand> {
    private final String brand;
    private final double minPrice;
    private final double maxPrice;

    public BrandPriceFilter(String brand, double minPrice, double maxPrice) {
        this.brand = brand;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    @Override
    public boolean test(CarBrand car) {
        return car.brandType().equalsIgnoreCase(brand)
            && car.price().amount() >= minPrice
            && car.price().amount() <= maxPrice;
    }
}
