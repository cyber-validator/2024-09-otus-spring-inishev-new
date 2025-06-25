package ru.otus.hw.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CreateOrEditBookDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LibraryControllerTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    @Order(1)
    void allBooks() {
        webClient.get().uri("/api/books")
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .hasSize(3);
    }

    @Test
    @Order(2)
    void saveBook() {
        String authorId = "2";
        String title = "new-title";
        List<String> genreIds = List.of("1");
        CreateOrEditBookDto dto = new CreateOrEditBookDto(null, title, authorId, genreIds);
        webClient.post().uri("/api/books").bodyValue(dto)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .value(book -> {
                    assertThat(book.getId()).isNotBlank();
                    assertThat(book.getTitle()).isEqualTo(title);
                    assertThat(book.getAuthor().getId()).isEqualTo(authorId);
                    assertThat(book.getGenres()).hasSize(genreIds.size());
                });
    }

    @Test
    @Order(3)
    void bookById() {
        webClient.get().uri("/api/books/2")
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .value(book -> {
                    assertThat(book.getId()).isEqualTo("2");
                    assertThat(book.getTitle()).isEqualTo("BookTitle_2");
                    assertThat(book.getAuthor().getFullName()).isEqualTo("Author_2");
                    assertThat(book.getGenres()).hasSize(2);
                });
    }

    @Test
    @Order(4)
    void removeBook() {
        webClient.delete().uri("/api/books/2")
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();
    }

}