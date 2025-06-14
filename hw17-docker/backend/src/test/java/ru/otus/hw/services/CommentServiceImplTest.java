package ru.otus.hw.services;

import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Сервис для работы с комментариями")
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import({
        BookServiceImpl.class,
        CommentServiceImpl.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CommentServiceImplTest {
    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private CommentServiceImpl commentService;


    @DisplayName("должен найти все комментарии книги")
    @Test
    void shouldFindAllCommentsByBookId() {
        var bookId = 1L;
        var expectedComments = List.of(
                new Comment(1, "Comment-1_book-1", new Book(bookId, null, null, null)),
                new Comment(2, "Comment-2_book-1", new Book(bookId, null, null, null)));
        var comments = commentService.findAllCommentsByBookId(bookId);
        assertThat(comments).hasSameElementsAs(expectedComments);
    }

    @DisplayName("должен найти комментарий по id")
    @Test
    void shouldGetCommentById() {
        var expectedComment = new Comment(3,
                "Comment-1_book-3",
                new Book(3L, null, null, null));
        var comment = commentService.getCommentById(3L);
        assertThat(comment.isPresent()).isTrue();
        assertThat(comment.get()).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);
    }

    @DisplayName("должен выбросить исключение при обращении к названию книги из комментария")
    @Test
    void shouldThrowLazyInitializationExceptionWhenGetBookTitleFromFoundCommentById() {
        var foundComment = commentService.getCommentById(3L);
        assertThat(foundComment.isPresent()).isTrue();
        assertThrows(LazyInitializationException.class, () -> foundComment.get().getBook().getTitle());
    }

    @DisplayName("должны удалиться комментарии из книги при удалении последней")
    @Test
    void shouldDeleteBookCommentsWhenDeleteBook() {
        var bookId = 1L;
        var foundComment = commentService.getCommentById(bookId);
        assertThat(foundComment.isPresent()).isTrue();
        bookService.deleteById(bookId);
        foundComment = commentService.getCommentById(bookId);
        assertThat(foundComment.isPresent()).isFalse();
    }

}