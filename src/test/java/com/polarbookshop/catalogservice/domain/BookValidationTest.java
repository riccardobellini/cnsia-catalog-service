package com.polarbookshop.catalogservice.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BookValidationTest {
    private static Validator validator;

    @BeforeAll
    static void setup() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void whenAllFieldsCorrectThenValidationSucceeds() {
        final var book = Book.of("1324567891",
                "Title",
                "Author",
                9.90,
                "Polarsophia");
        final Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenIsbnDefinedButIncorrectThenValidationFails() {
        final Book book = Book.of("AD12123FWW43",
                "Title",
                "Author",
                9.90,
                "Polarsophia");
        final Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isNotEmpty();
        assertThat(violations).hasSize(1);
        assertThat(violations.stream().findFirst().get().getMessage()).isEqualTo("The ISBN is invalid");

    }
}
