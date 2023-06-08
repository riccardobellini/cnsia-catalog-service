package com.polarbookshop.catalogservice.exception;

public class BookAlreadyExistsException extends CatalogServiceException {
    private final String isbn;

    public BookAlreadyExistsException(String isbn) {
        super();
        this.isbn = isbn;
    }

    @Override
    public String getMessage() {
        return "Book with isbn \"%s\" already exists".formatted(isbn);
    }

}
