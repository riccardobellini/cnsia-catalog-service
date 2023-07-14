package com.polarbookshop.catalogservice.domain;

import com.polarbookshop.catalogservice.config.DataConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
class BookRepositoryJdbcTests {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;

    @Test
    void findBookByIsbnWhenExisting() {
        final String isbn = "1234561237";
        final Book book = Book.of(isbn,
                "Title",
                "Author",
                9.9,
                "Polarsophia");
        jdbcAggregateTemplate.insert(book);
        final Optional<Book> actualBook = bookRepository.findByIsbn(isbn);
        assertThat(actualBook).isPresent();
        assertThat(actualBook.get().isbn()).isEqualTo(isbn);
    }

    @Test
    void whenCreateBookNotAuthenticatedThenNoAuditMetadata() {
        final String isbn = "1234561237";
        final Book toCreate = Book.of(isbn,
                "Title",
                "Author",
                9.9,
                "Polarsophia");
        final Book createdBook = bookRepository.save(toCreate);
        assertThat(createdBook.createdBy()).isNull();
        assertThat(createdBook.lastModifiedBy()).isNull();
    }

    @Test
    @WithMockUser("John")
    void whenCreateBookAuthenticatedThenAuditMetadata() {
        final String isbn = "1234561237";
        final Book toCreate = Book.of(isbn,
                "Title",
                "Author",
                9.9,
                "Polarsophia");
        final Book createdBook = bookRepository.save(toCreate);
        assertThat(createdBook.createdBy()).isEqualTo("John");
        assertThat(createdBook.lastModifiedBy()).isEqualTo("John");
    }
}
