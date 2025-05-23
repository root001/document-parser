package com.abdulbasit.adebayo.docparser.filter;

import com.abdulbasit.adebayo.docparser.model.CarBrand;
import com.abdulbasit.adebayo.docparser.model.Price;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BrandPriceFilterTest {
    private static final LocalDate DATE = LocalDate.of(2022, 11, 20);
    private static final Price PRICE_25K = new Price("USD", 25000);
    private static final Price PRICE_18K = new Price("USD", 18000);
    private static final Price PRICE_35K = new Price("USD", 35000);
    private static final List<Price> PRICE_LIST = List.of(PRICE_25K);

    @Test
    void test_ToyotaAt25k_ShouldInclude() {
        BrandPriceFilter filter = new BrandPriceFilter("Toyota", 20000, 30000);
        CarBrand car = new CarBrand("SUV", "Toyota", "RAV4", DATE, PRICE_25K, PRICE_LIST);
        assertTrue(filter.test(car));
    }

    @Test
    void test_HondaAt18k_ShouldExclude() {
        BrandPriceFilter filter = new BrandPriceFilter("Toyota", 20000, 30000);
        CarBrand car = new CarBrand("SUV", "Honda", "CR-V", DATE, PRICE_18K, PRICE_LIST);
        assertFalse(filter.test(car));
    }

    @Test
    void test_ToyotaAt35k_ShouldExclude() {
        BrandPriceFilter filter = new BrandPriceFilter("Toyota", 20000, 30000);
        CarBrand car = new CarBrand("SUV", "Toyota", "Highlander", DATE, PRICE_35K, PRICE_LIST);
        assertFalse(filter.test(car));
    }

    @Test
    void test_CaseInsensitiveBrandMatch() {
        BrandPriceFilter filter = new BrandPriceFilter("toyota", 20000, 30000);
        CarBrand car = new CarBrand("SUV", "TOYOTA", "RAV4", DATE, PRICE_25K, PRICE_LIST);
        assertTrue(filter.test(car));
    }

    @Test
    void test_NullPrice_ShouldExclude() {
        BrandPriceFilter filter = new BrandPriceFilter("Toyota", 20000, 30000);
        CarBrand car = new CarBrand("SUV", "Toyota", "RAV4", DATE, null, PRICE_LIST);
        assertTrue(filter.test(car));
    }

    @Test
    void test_PriceListOnly_ShouldUseFirstPrice() {
        BrandPriceFilter filter = new BrandPriceFilter("Toyota", 20000, 30000);
        List<Price> prices = List.of(PRICE_25K, PRICE_35K);
        CarBrand car = new CarBrand("SUV", "Toyota", "RAV4", DATE, null, prices);
        assertTrue(filter.test(car));
    }
}
