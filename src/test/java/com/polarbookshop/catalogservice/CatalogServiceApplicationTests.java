package com.polarbookshop.catalogservice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.polarbookshop.catalogservice.config.SecurityConfig;
import com.polarbookshop.catalogservice.domain.Book;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@Import(SecurityConfig.class)
@Testcontainers
class CatalogServiceApplicationTests {

    private record KeycloakToken(String accessToken) {
        @JsonCreator
        private KeycloakToken(@JsonProperty("access_token") final String accessToken) {
            this.accessToken = accessToken;
        }
    }

    @Autowired
    private WebTestClient webTestClient;

    private static KeycloakToken isabelleToken;
    private static KeycloakToken bjornToken;

    @Container
    private static final KeycloakContainer keycloak =
            new KeycloakContainer("quay.io/keycloak/keycloak:21.1.2")
                    .withRealmImportFile("test-realm-config.json");

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> keycloak.getAuthServerUrl() + "realms/PolarBookshop");
    }

    @BeforeAll
    static void generateAccessTokens() {
        final WebClient webClient = WebClient.builder()
                .baseUrl(keycloak.getAuthServerUrl() + "realms/PolarBookshop/protocol/openid-connect/token")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        isabelleToken = authenticateWith("isabelle", "password", webClient);
        bjornToken = authenticateWith("bjorn", "password", webClient);
    }

    private static KeycloakToken authenticateWith(String user, String password, WebClient webClient) {
        return webClient
                .post()
                .body(BodyInserters.fromFormData("grant_type", "password")
                        .with("client_id", "polar-test")
                        .with("username", user)
                        .with("password", password))
                .retrieve()
                .bodyToMono(KeycloakToken.class)
                .block();
    }

    @Test
    void whenPostRequestThenBookCreated() {
        final Book book = Book.of("1233211231",
                "Title",
                "Author",
                BigDecimal.valueOf(9.90),
                "Polarsophia");
        webTestClient
                .post()
                .uri("/books")
                .headers(headers ->
                        headers.setBearerAuth(isabelleToken.accessToken()))
                .bodyValue(book)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class).value(actualBook -> {
                    assertThat(actualBook).isNotNull();
                    assertThat(actualBook.isbn()).isEqualTo(book.isbn());
                });
    }

    @Test
    void whenPostRequestUnauthorizedThenHttp403() {
        final Book book = Book.of("1233211231",
                "Title",
                "Author",
                BigDecimal.valueOf(9.90),
                "Polarsophia");
        webTestClient
                .post()
                .uri("/books")
                .headers(headers ->
                        headers.setBearerAuth(bjornToken.accessToken()))
                .bodyValue(book)
                .exchange()
                .expectStatus().isForbidden();
    }

}
