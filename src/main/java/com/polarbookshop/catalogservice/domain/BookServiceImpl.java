package com.polarbookshop.catalogservice.domain;

import com.polarbookshop.catalogservice.exception.BookAlreadyExistsException;
import com.polarbookshop.catalogservice.exception.BookNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {


    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Iterable<Book> viewList() {
        return bookRepository.findAll();
    }

    @Override
    public Book viewDetails(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException(isbn));
    }

    @Override
    public Book addToCatalog(Book book) {
        if (bookRepository.existsByIsbn(book.isbn())) {
            throw new BookAlreadyExistsException(book.isbn());
        }
        return bookRepository.save(book);
    }

    @Override
    public void removeFromCatalog(String isbn) {
        bookRepository.deleteByIsbn(isbn);
    }

    @Override
    public Book editDetails(String isbn, Book book) {
        return bookRepository.findByIsbn(isbn)
                .map(existingBook -> {
                    final Book bookToUpdate = new Book(existingBook.id(), existingBook.isbn(),
                            book.title(),
                            book.author(),
                            book.price(),
                            existingBook.createdDate(),
                            existingBook.lastModifiedDate(),
                            existingBook.version());
                    return bookRepository.save(bookToUpdate);
                })
                .orElseGet(() -> bookRepository.save(book));
    }
}
