package com.polarbookshop.catalogservice.demo;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@ConditionalOnProperty(value = "polar.test-data.enabled")
public class BookDataLoader {

    private final BookRepository bookRepository;

    public BookDataLoader(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadTestData() {
        bookRepository.deleteAll();

        final var book1 = Book.of("1234567891",
                "Northern Lights",
                "Lyra Silverstar",
                BigDecimal.valueOf(9.90));
        final var book2 = Book.of("1234567892",
                "Polar Journey",
                "Iorek Polarson",
                BigDecimal.valueOf(12.90));

        bookRepository.saveAll(List.of(book1, book2));
    }
}
