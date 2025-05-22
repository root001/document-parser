package com.abdulbasit.adebayo.docparser.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "car")
public record Car(
    String type,
    String model,
    Price price,
    List<Price> priceList
) {
    public Car {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Brand cannot be empty");
        }
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("Model cannot be empty");
        }
    }
}
