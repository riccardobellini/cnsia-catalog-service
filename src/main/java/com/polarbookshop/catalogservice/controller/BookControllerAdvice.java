package com.polarbookshop.catalogservice.controller;

import com.polarbookshop.catalogservice.exception.BookAlreadyExistsException;
import com.polarbookshop.catalogservice.exception.BookNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class BookControllerAdvice {
    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String bookNotFoundHandler(BookNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(BookAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String bookAlreadyExistsHandler(BookAlreadyExistsException e) {
        return e.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> bookAlreadyExistsHandler(MethodArgumentNotValidException e) {
        final var errors = new HashMap<String, String>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            final String field = ((FieldError) error).getField();
            final String errorMessage = error.getDefaultMessage();
            errors.put(field, errorMessage);
        });
        return errors;
    }
}
