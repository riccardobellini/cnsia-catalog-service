package com.polarbookshop.catalogservice.controller;

import com.polarbookshop.catalogservice.config.SecurityConfig;
import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookService;
import com.polarbookshop.catalogservice.exception.BookNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(SecurityConfig.class)
public class BookControllerMvcTests {
    private static final String ROLE_CUSTOMER = "ROLE_customer";
    private static final String ROLE_EMPLOYEE = "ROLE_employee";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Test
    void whenGetBookNotExistingAndAuthenticatedThenShouldReturn404() throws Exception {
        final String isbn = "1233211231";
        given(bookService.viewDetails(isbn))
                .willThrow(new BookNotFoundException(isbn));
        mockMvc.perform(get("/books/{isbn}", isbn)
                        .with(jwt()))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetBookNotExistingAndNotAuthenticatedThenShouldReturn404() throws Exception {
        final String isbn = "1233211231";
        given(bookService.viewDetails(isbn))
                .willThrow(new BookNotFoundException(isbn));
        mockMvc.perform(get("/books/{isbn}", isbn))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenDeleteBookWithEmployeeRoleThenHttp204() throws Exception {
        final String isbn = "1233211231";
        mockMvc.perform(delete("/books/%s".formatted(isbn))
                        .with(jwt()
                                .authorities(new SimpleGrantedAuthority(ROLE_EMPLOYEE))))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenGetBookExistingAndAuthenticatedThenShouldReturn200() throws Exception {
        final String isbn = "7373731394";
        final Book expectedBook = Book.of(isbn, "Title", "Author", 9.90, "Polarsophia");
        given(bookService.viewDetails(isbn)).willReturn(expectedBook);
        mockMvc
                .perform(get("/books/" + isbn)
                        .with(jwt()))
                .andExpect(status().isOk());
    }

    @Test
    void whenDeleteBookWithCustomerRoleThenHttp204() throws Exception {
        final String isbn = "1233211231";
        mockMvc.perform(delete("/books/%s".formatted(isbn))
                        .with(jwt()
                                .authorities(new SimpleGrantedAuthority(ROLE_CUSTOMER))))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenDeleteBookNotAuthenticatedThenHttp204() throws Exception {
        final String isbn = "1233211231";
        mockMvc.perform(delete("/books/%s".formatted(isbn)))
                .andExpect(status().isUnauthorized());
    }
}
