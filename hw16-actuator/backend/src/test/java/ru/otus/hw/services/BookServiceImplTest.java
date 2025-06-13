package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с книгами")
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import(BookServiceImpl.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class BookServiceImplTest {

    @Autowired
    private BookServiceImpl bookService;


    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("должен находить книгу по ее id")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void shouldFindById(Book book) {
        var expectedBook = dbBooks.get(dbBooks.indexOf(book));
        var foundBook = bookService.findById(book.getId());
        assertThat(foundBook).isPresent();
        assertThat(foundBook).get().usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldFindAll() {
        var expectedBooks = dbBooks;
        var foundBooks = bookService.findAll();
        assertThat(foundBooks).hasSameElementsAs(expectedBooks);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldInsert() {
        String title = "test title";
        var author = dbAuthors.get(2);
        var genres = dbGenres.stream().map(Genre::getId).collect(Collectors.toSet());

        var newBook = bookService.update(0, title, author.getId(), genres);
        assertThat(newBook).isNotNull()
                .matches(b -> b.getId() > 0)
                .matches(b -> b.getTitle().equals(title))
                .matches(b -> b.getAuthor().equals(author));
        newBook.getGenres().forEach(genre -> assertThat(genres.contains(genre.getId())).isTrue());
    }

    @DisplayName("должен обновить существующую книгу")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldUpdate() {
        var book = dbBooks.get(0);
        var foundBook = bookService.findById(book.getId());
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get()).usingRecursiveComparison().isEqualTo(book);

        String newTitle = "new title";
        long newAuthorId = 3L;
        var newGenres = Set.of(3L, 4L);

        var updatedBook = bookService.update(book.getId(), newTitle, newAuthorId, newGenres);
        assertThat(updatedBook)
                .matches(b -> b.getTitle().equals(newTitle))
                .matches(b -> b.getAuthor().getId() == newAuthorId);
        updatedBook.getGenres().forEach(genre -> assertThat(newGenres.contains(genre.getId())).isTrue());
    }

    @DisplayName("должен удалить книгу по id")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteById() {
        var bookToDelete = dbBooks.get(dbBooks.indexOf(dbBooks.get(0)));
        bookService.deleteById(bookToDelete.getId());
        var deletedBook = bookService.findById(bookToDelete.getId());
        assertThat(deletedBook.isEmpty()).isTrue();
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

}