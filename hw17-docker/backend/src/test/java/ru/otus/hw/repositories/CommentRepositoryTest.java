package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Spring Data репозиторий для работы с комментариями ")
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class CommentRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private CommentRepository commentRepository;

    @DisplayName("должен загружать все комментарии по id книги")
    @ParameterizedTest
    @MethodSource("getBookIds")
    void shouldFindAllCommentsByBookId(long bookId) {
        var expectedComments = em.getEntityManager()
                .createQuery("select c from Comment c where c.book.id = :bookId", Comment.class)
                .setParameter("bookId", bookId)
                .getResultList();
        var foundComments = commentRepository.findAllByBookId(bookId);
        assertThat(foundComments).usingRecursiveComparison().isEqualTo(expectedComments);
        System.out.println(foundComments);
    }

    public static List<Long> getBookIds() {
        return LongStream.range(1, 4).boxed().toList();
    }

}