package com.abdulbasit.adebayo.docparser.model;

import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.abdulbasit.adebayo.docparser.util.DateFormatter;

public record CarBrand(
    String brandType,
    String productName,
    String model,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy,dd,MM")
    LocalDate releaseDate,
    Price price,
    List<Price> priceList
) {
    public CarBrand {
        if (brandType == null || brandType.isBlank()) {
            throw new IllegalArgumentException("Brand name cannot be empty");
        }
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (releaseDate == null) {
            throw new IllegalArgumentException("Release date cannot be null");
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %s %s",
                brandType,
                productName,
                model,
                DateFormatter.formatForOutput(releaseDate),
                price);
    }
}
