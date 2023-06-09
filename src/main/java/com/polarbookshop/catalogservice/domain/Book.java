package com.polarbookshop.catalogservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.math.BigDecimal;
import java.time.Instant;

public record Book(
        @Id
        Long id,
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
        BigDecimal price,
        @CreatedDate
        Instant createdDate,
        @LastModifiedDate
        Instant lastModifiedDate,
        @Version
        @JsonIgnore
        int version) {

    public static Book of(String isbn, String title, String author, BigDecimal price) {
        return new Book(null,
                isbn,
                title,
                author,
                price,
                null,
                null,
                0);
    }
}
