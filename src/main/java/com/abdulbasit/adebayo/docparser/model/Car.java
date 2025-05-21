package com.abdulbasit.adebayo.docparser.model;

public record Car(
    String brand,
    String model,
    double price
) {
    public Car {
        if (brand == null || brand.isBlank()) {
            throw new IllegalArgumentException("Brand cannot be empty");
        }
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("Model cannot be empty");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }
}
