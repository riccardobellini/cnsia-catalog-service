package com.polarbookshop.catalogservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record Book(
        @NotBlank(message = "The ISBN cannot be empty")
        @Pattern(regexp = "^([0-9]{10}|[0-9]{13})$",
                message = "The ISBN is invalid")
        String isbn,
        @NotBlank(message = "The title cannot be empty")
        String title,
        @NotBlank(message = "The author cannot be empty")
        String author,
        @NotNull(message = "Book price must be defined")
        @Positive(message = "Book price must be greater than 0")
        BigDecimal price) {
}
