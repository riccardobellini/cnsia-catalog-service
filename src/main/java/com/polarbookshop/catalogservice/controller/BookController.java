package com.polarbookshop.catalogservice.controller;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("books")
@Slf4j
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public Iterable<Book> getAll() {
        log.info("Fetching the list of books in the catalog");
        return bookService.viewList();
    }

    @GetMapping("{isbn}")
    public Book getByIsbn(@PathVariable("isbn") String isbn) {
        log.info("Fetching the book with ISBN {} from the catalog", isbn);
        return bookService.viewDetails(isbn);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@Valid @RequestBody Book book) {
        log.info("Adding a new book to the catalog with ISBN {}", book.isbn());
        return bookService.addToCatalog(book);
    }

    @DeleteMapping("{isbn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("isbn") String isbn) {
        log.info("Deleting book with ISBN {}", isbn);
        bookService.removeFromCatalog(isbn);
    }

    @PutMapping("{isbn}")
    public Book update(@PathVariable("isbn") String isbn, @Valid @RequestBody Book book) {
        log.info("Updating book with ISBN {}", isbn);
        return bookService.editDetails(isbn, book);
    }
}
