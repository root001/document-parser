package com.abdulbasit.adebayo.docparser.filter;

import com.abdulbasit.adebayo.docparser.model.Car;
import com.abdulbasit.adebayo.docparser.model.CarBrand;
import com.abdulbasit.adebayo.docparser.model.Price;
import org.springframework.stereotype.Component;

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
        if (car == null || !car.productName().equalsIgnoreCase(brand)) {
            return false;
        }

        // Check price from price() or first price in priceList
        Price price = car.price();
        if (price == null && car.priceList() != null && !car.priceList().isEmpty()) {
            price = car.priceList().get(0);
        }
        if (price == null) {
            return false;
        }

        return price.amount() >= minPrice && price.amount() <= maxPrice;
    }
}
