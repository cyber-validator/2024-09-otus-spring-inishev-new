package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.listeners.CommentCascadeRemoveEventListener;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с комментариями")
@DataMongoTest
@Import({CommentCascadeRemoveEventListener.class,
        BookServiceImpl.class,
        CommentServiceImpl.class,
        CommentConverter.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CommentServiceImplTest {

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private CommentConverter commentConverter;

    @DisplayName("должен найти все комментарии книги")
    @Test
    void shouldFindAllCommentsByBookId() {
        var bookId = "1";
        var expectedComments = List.of(
                new Comment("1", "Comment-1_book-1", new Book(bookId, null, null, null)),
                new Comment("2", "Comment-2_book-1", new Book(bookId, null, null, null)));
        var r = commentService.findAllCommentsByBookId(bookId);
        assertThat(r).hasSameElementsAs(expectedComments);
        r.forEach(c -> System.out.println(commentConverter.commentToString(c)));
    }

    @DisplayName("должен найти комментарий по id")
    @Test
    void shouldGetCommentById() {
        var expectedComment = new Comment("3",
                "Comment-1_book-3",
                new Book("3", null, null, null));
        var r = commentService.getCommentById("3");
        assertThat(r.isPresent()).isTrue();
        assertThat(r.get()).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);
        System.out.println(commentConverter.commentToString(r.get()));
    }

    @DisplayName("должны удалиться комментарии из книги при удалении последней")
    @Test
    void shouldDeleteBookCommentsWhenDeleteBook() {
        var bookId = "3";
        var foundComment = commentService.getCommentById(bookId);
        assertThat(foundComment.isPresent()).isTrue();
        bookService.deleteById(bookId);
        foundComment = commentService.getCommentById(bookId);
        assertThat(foundComment.isPresent()).isFalse();
    }

}