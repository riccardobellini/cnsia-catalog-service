package com.polarbookshop.catalogservice.domain;

public interface BookService {
    Iterable<Book> viewList();

    Book viewDetails(String isbn);

    Book addToCatalog(Book book);

    void removeFromCatalog(String isbn);

    Book editDetails(String isbn, Book book);
}
