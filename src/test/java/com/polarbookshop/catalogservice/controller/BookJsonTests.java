package com.polarbookshop.catalogservice.controller;

import com.polarbookshop.catalogservice.domain.Book;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.util.BigDecimalComparator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookJsonTests {
    @Autowired
    private JacksonTester<Book> json;

    @Test
    void testSerialize() throws IOException {
        final Instant now = Instant.now();
        final Book book = new Book(1L,
                "1231231231",
                "Title",
                "Author",
                9.90,
                "Polarsophia",
                now,
                now,
                "John",
                "John",
                0);
        final JsonContent<Book> jsonContent = json.write(book);
        assertThat(jsonContent).extractingJsonPathNumberValue("@.id")
                .usingComparator(Comparator.comparingLong(Number::longValue)).isEqualTo(1L);
        assertThat(jsonContent).extractingJsonPathStringValue("@.isbn")
                .isEqualTo(book.isbn());
        assertThat(jsonContent).extractingJsonPathStringValue("@.author")
                .isEqualTo(book.author());
        assertThat(jsonContent).extractingJsonPathStringValue("@.title")
                .isEqualTo(book.title());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.price")
                .isEqualTo(9.90);
        assertThat(jsonContent).extractingJsonPathStringValue("@.publisher")
                .isEqualTo(book.publisher());
        assertThat(jsonContent).extractingJsonPathStringValue("@.createdDate")
                .isEqualTo(DateTimeFormatter.ISO_INSTANT.format(now));
        assertThat(jsonContent).extractingJsonPathStringValue("@.lastModifiedDate")
                .isEqualTo(DateTimeFormatter.ISO_INSTANT.format(now));
        assertThat(jsonContent).extractingJsonPathStringValue("@.createdBy")
                .isEqualTo("John");
        assertThat(jsonContent).extractingJsonPathStringValue("@.lastModifiedBy")
                .isEqualTo("John");
    }

    @Test
    void testDeserialize() throws IOException {
        final var content = """
                {
                    "id" : 1,
                    "isbn": "1234567890",
                    "title": "Title",
                    "author": "Author",
                    "createdDate": "2023-06-09T12:55:00Z",
                    "lastModifiedDate": "2023-06-09T12:55:00Z",
                    "publisher": "Polarsophia",
                    "price": 9.90,
                    "createdBy": "John",
                    "lastModifiedBy": "John"
                }
                """;
        assertThat(json.parse(content))
                .usingRecursiveComparison(RecursiveComparisonConfiguration.builder()
                        .withComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                        .build())
                .isEqualTo(new Book(1L,
                        "1234567890",
                        "Title",
                        "Author",
                        9.90,
                        "Polarsophia",
                        LocalDateTime.of(2023, 6, 9, 12, 55, 0).toInstant(ZoneOffset.UTC),
                        LocalDateTime.of(2023, 6, 9, 12, 55, 0).toInstant(ZoneOffset.UTC),
                        "John",
                        "John",
                        0));
    }
}
