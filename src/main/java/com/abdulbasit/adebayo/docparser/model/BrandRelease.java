package com.abdulbasit.adebayo.docparser.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public record BrandRelease(
    String brandName,
    String productName,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy,dd,MM")
    LocalDate releaseDate,
    String version
) {
    public BrandRelease {
        if (brandName == null || brandName.isBlank()) {
            throw new IllegalArgumentException("Brand name cannot be empty");
        }
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (releaseDate == null) {
            throw new IllegalArgumentException("Release date cannot be null");
        }
    }
}
