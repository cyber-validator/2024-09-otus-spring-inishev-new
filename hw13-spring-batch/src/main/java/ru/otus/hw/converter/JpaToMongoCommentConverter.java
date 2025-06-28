package ru.otus.hw.converter;

import org.springframework.stereotype.Component;
import ru.otus.hw.entity.jpa.JpaComment;
import ru.otus.hw.entity.mongo.MongoBook;
import ru.otus.hw.entity.mongo.MongoComment;

@Component
public class JpaToMongoCommentConverter extends ValueToStringConverter {

    public MongoComment convert(JpaComment comment) {
        MongoBook mongoBook = new MongoBook();
        mongoBook.setId(stringOf(comment.getBook().getId()));

        MongoComment mongoComment = new MongoComment();
        mongoComment.setId(stringOf(comment.getId()));
        mongoComment.setMessage(comment.getMessage());
        mongoComment.setBook(mongoBook);

        return mongoComment;
    }

}
