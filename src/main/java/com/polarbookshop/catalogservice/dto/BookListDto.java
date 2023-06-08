package com.polarbookshop.catalogservice.dto;

import com.polarbookshop.catalogservice.domain.Book;

public record BookListDto(Iterable<Book> books) {
}
