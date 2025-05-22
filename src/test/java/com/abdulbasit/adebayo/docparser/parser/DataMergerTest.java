package com.abdulbasit.adebayo.docparser.parser;

import com.abdulbasit.adebayo.docparser.filter.BrandPriceFilter;
import com.abdulbasit.adebayo.docparser.model.Brand;
import com.abdulbasit.adebayo.docparser.model.Car;
import com.abdulbasit.adebayo.docparser.model.CarBrand;
import com.abdulbasit.adebayo.docparser.model.Price;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DataMergerTest {
    private final DataMerger merger = new DataMerger();

    @Test
    void mergeData_WithMatchingProduct_InheritsCsvDate() {
        List<Brand> csvData = List.of(
                new Brand("Toyota", LocalDate.of(2023, 1, 15)),
                        new Brand("Honda", LocalDate.of(2024, 1, 15))
        );
        List<Price> prices = List.of(new Price("EUR", 2900), new Price("GBP", 2100) );
        List<Car> xmlData = List.of(
                new Car("SUV", "RAV4", new Price("USD", 2900), prices ),
                new Car("Truck", "CR-V", new Price("USD", 2900), prices)
        );

        List<CarBrand> result = merger.mergeData(csvData, xmlData, null);
        
        assertEquals(LocalDate.of(2023, 1, 15), result.get(0).releaseDate());
    }

    @Test
    void mergeData_WithFilter_FiltersResults() {
        List<Brand> csvData = List.of(
            new Brand("Toyota", LocalDate.of(2023, 1, 15) )
        );
        List<Price> prices = List.of(new Price("EUR", 2900), new Price("GBP", 2100) );
        List<Car> xmlData = List.of(
            new Car("SUV", "RAV4", new Price("USD", 2900), prices ),
            new Car("Truck", "CR-V", new Price("USD", 2900), prices)
        );

        BrandPriceFilter filter = new BrandPriceFilter("Toyota", 20000, 30000);
        List<CarBrand> result = merger.mergeData(csvData, xmlData, filter);
        
        assertEquals(1, result.size());
        assertEquals("Toyota", result.getFirst().brandType());
    }

    @Test
    void mergeData_WithMissingProduct_KeepsXmlDate() {
        List<Brand> csvData = List.of(
            new Brand("Toyota", LocalDate.of(2023, 1, 15) )
        );

        List<Price> prices = List.of(new Price("EUR", 2900), new Price("GBP", 2100) );
        List<Car> xmlData = List.of(
                new Car("SUV", "RAV4", new Price("USD", 2900), prices )
        );

        List<CarBrand> result = merger.mergeData(csvData, xmlData, null);
        assertEquals(LocalDate.of(2023, 3, 1), result.get(0).releaseDate());
    }

    @Test
    void mergeData_WithNullXmlDate_UsesCsvDate() {
        List<Brand> csvData = List.of(
                new Brand("Toyota", LocalDate.of(2023, 1, 15) )
        );

        List<Price> prices = List.of(new Price("EUR", 2900), new Price("GBP", 2100) );
        List<Car> xmlData = List.of(
                new Car("SUV", "RAV4", new Price("USD", 2900), prices )
        );

        List<CarBrand> result = merger.mergeData(csvData, xmlData, null);
        assertEquals(LocalDate.of(2023, 1, 15), result.get(0).releaseDate());
    }
}
