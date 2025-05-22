package com.abdulbasit.adebayo.docparser.filter;

import com.abdulbasit.adebayo.docparser.model.Car;
import com.abdulbasit.adebayo.docparser.model.CarBrand;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BrandPriceFilterTest {
    String dateStr = "11/20/2022";
    LocalDate date = LocalDate.parse(dateStr);

    @Test
    void test_ToyotaAt25k_ShouldInclude() {
        BrandPriceFilter filter = new BrandPriceFilter("Toyota", 20000, 30000);
        CarBrand car = new CarBrand("Toyota", "SUV", "RAV4", date, 25000 );
        assertTrue(filter.test(car));
    }

    @Test
    void test_HondaAt18k_ShouldExclude() {
        BrandPriceFilter filter = new BrandPriceFilter("Toyota", 20000, 30000);
        CarBrand car = new CarBrand("Honda", "CR-V", 18000, date);
        assertFalse(filter.test(car));
    }

    @Test
    void test_ToyotaAt35k_ShouldExclude() {
        BrandPriceFilter filter = new BrandPriceFilter("Toyota", 20000, 30000);
        Car car = new Car("Toyota", "Highlander", 35000, date);
        assertFalse(filter.test(car));
    }

    @Test
    void test_CaseInsensitiveBrandMatch() {
        BrandPriceFilter filter = new BrandPriceFilter("toyota", 20000, 30000);
        Car car = new Car("TOYOTA", "RAV4", 25000, date);
        assertTrue(filter.test(car));
    }
}
