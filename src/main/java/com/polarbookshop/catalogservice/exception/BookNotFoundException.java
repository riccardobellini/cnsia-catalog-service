package com.polarbookshop.catalogservice.exception;

public class BookNotFoundException extends CatalogServiceException {
    private final String isbn;

    public BookNotFoundException(String isbn) {
        super();
        this.isbn = isbn;
    }

    @Override
    public String getMessage() {
        return "Book with isbn \"%s\" could not be found".formatted(isbn);
    }
}
