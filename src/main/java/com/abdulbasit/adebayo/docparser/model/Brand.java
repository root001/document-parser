package com.abdulbasit.adebayo.docparser.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record Brand(
        String productName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "mm/dd/yyyy")
        LocalDate releaseDate) {
}
