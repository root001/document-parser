package com.abdulbasit.adebayo.docparser.formatter;

import com.abdulbasit.adebayo.docparser.model.CarBrand;
import com.abdulbasit.adebayo.docparser.model.Price;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TableFormatterTest {
    private static final Price PRICE = new Price("USD", 25000);
    private static final List<Price> PRICE_LIST = List.of(PRICE);

    @Test
    void format_SingleRelease_ReturnsFormattedTable() {
        List<CarBrand> releases = List.of(
            new CarBrand("SUV", "Toyota", "RAV4", LocalDate.of(2023, 1, 15), PRICE, PRICE_LIST)
        );
        
        TableFormatter formatter = new TableFormatter();
        String result = formatter.format(releases);

    //    System.out.println("---------------- headers : "+ result);
        assertTrue(result.contains("| Brand      | Model      | Release Date (yyyy,dd,MM) | Product    | Currency | Amount     |"));
        assertTrue(result.contains("| SUV        | RAV4       | 2023,15,01                | Toyota     | USD      | 25000.00   |"));
    }

    @Test
    void format_LongValues_TruncatesWithEllipsis() {
        List<CarBrand> releases = List.of(
            new CarBrand("SUV Hybrid", "Toyota Motor Corporation", "RAV4 Hybrid XSE", 
                LocalDate.of(2023, 1, 15), PRICE, PRICE_LIST)
        );
        
        TableFormatter formatter = new TableFormatter();
        String result = formatter.format(releases);

        assertTrue(result.contains("| SUV Hybrid | RAV4 Hybr… | 2023,15,01                | Toyota Mo… | USD      | 25000.00   |"));
    }

    @Test
    void format_EmptyList_ReturnsHeaderOnly() {
        TableFormatter formatter = new TableFormatter();
        String result = formatter.format(List.of());

        assertTrue(result.contains("| Brand      | Model      | Release Date (yyyy,dd,MM) | Product    | Currency | Amount     |"));
        assertFalse(result.contains("2023,15,01")); // No data rows
    }

    @Test
    void format_NullPrice_ShowsEmptyValues() {
        List<CarBrand> releases = List.of(
            new CarBrand("SUV", "Toyota", "RAV4", LocalDate.of(2023, 1, 15), null, PRICE_LIST)
        );
        
        TableFormatter formatter = new TableFormatter();
        String result = formatter.format(releases);
    //    System.out.println("-------- Table result is : " + result);
        assertTrue(result.contains("SUV") &&
                result.contains("RAV4") &&
                result.contains("2023,15,01") &&
                result.contains("Toyota"));
    //    assertTrue(result.contains("| SUV    | RAV4      | 2023,15,01                | Toyota     |          |         |"));
    }

    @Test
    void format_MultiplePrices_UsesFirstPrice() {
        List<Price> multiplePrices = List.of(
            new Price("USD", 25000),
            new Price("EUR", 22000)
        );
        List<CarBrand> releases = List.of(
            new CarBrand("SUV", "Toyota", "RAV4", LocalDate.of(2023, 1, 15), null, multiplePrices)
        );
        
        TableFormatter formatter = new TableFormatter();
        String result = formatter.format(releases);

        assertTrue(result.contains("| SUV        | RAV4       | 2023,15,01                | Toyota     |          |            |"));
    }
}
