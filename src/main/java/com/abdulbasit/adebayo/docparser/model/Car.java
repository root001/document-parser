package com.abdulbasit.adebayo.docparser.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "car")
public record Car(
    String brand,
    String model,
    double price,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy,dd,MM")
    LocalDate releaseDate
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
