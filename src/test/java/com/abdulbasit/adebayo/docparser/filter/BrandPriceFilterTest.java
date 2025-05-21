package com.abdulbasit.adebayo.docparser.filter;

import com.abdulbasit.adebayo.docparser.model.Car;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BrandPriceFilterTest {
    @Test
    void test_ToyotaAt25k_ShouldInclude() {
        BrandPriceFilter filter = new BrandPriceFilter("Toyota", 20000, 30000);
        Car car = new Car("Toyota", "RAV4", 25000);
        assertTrue(filter.test(car));
    }

    @Test
    void test_HondaAt18k_ShouldExclude() {
        BrandPriceFilter filter = new BrandPriceFilter("Toyota", 20000, 30000);
        Car car = new Car("Honda", "CR-V", 18000);
        assertFalse(filter.test(car));
    }

    @Test
    void test_ToyotaAt35k_ShouldExclude() {
        BrandPriceFilter filter = new BrandPriceFilter("Toyota", 20000, 30000);
        Car car = new Car("Toyota", "Highlander", 35000);
        assertFalse(filter.test(car));
    }

    @Test
    void test_CaseInsensitiveBrandMatch() {
        BrandPriceFilter filter = new BrandPriceFilter("toyota", 20000, 30000);
        Car car = new Car("TOYOTA", "RAV4", 25000);
        assertTrue(filter.test(car));
    }
}
