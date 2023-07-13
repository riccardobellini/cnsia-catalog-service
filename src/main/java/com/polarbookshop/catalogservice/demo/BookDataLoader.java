package com.polarbookshop.catalogservice.demo;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

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
                9.90,
                "Polarsophia");
        final var book2 = Book.of("1234567892",
                "Polar Journey",
                "Iorek Polarson",
                12.90,
                "Polarsophia");

        bookRepository.saveAll(List.of(book1, book2));
    }
}
