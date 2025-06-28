package ru.otus.hw.converter;

import org.springframework.stereotype.Component;
import ru.otus.hw.entity.mongo.MongoAuthor;
import ru.otus.hw.entity.mongo.MongoBook;
import ru.otus.hw.entity.mongo.MongoGenre;
import ru.otus.hw.entity.sql.SqlBook;
import ru.otus.hw.entity.sql.SqlGenre;

import java.util.ArrayList;
import java.util.List;

@Component
public class JpaToMongoBookConverter {

    public MongoBook convert(SqlBook book) {
        MongoAuthor mongoAuthor = getMongoAuthor(book);
        List<MongoGenre> mongoGenres = getMongoGenres(book);
        return getMongoBook(book, mongoAuthor, mongoGenres);
    }

    private MongoBook getMongoBook(SqlBook book, MongoAuthor mongoAuthor, List<MongoGenre> mongoGenres) {
        MongoBook mongoBook = new MongoBook();
        mongoBook.setId(stringOf(book.getId()));
        mongoBook.setTitle(book.getTitle());
        mongoBook.setAuthor(mongoAuthor);
        mongoBook.setGenres(mongoGenres);
        return mongoBook;
    }

    private List<MongoGenre> getMongoGenres(SqlBook book) {
        List<MongoGenre> mongoGenres = new ArrayList<>();
        for (SqlGenre genre: book.getGenres()) {
            MongoGenre mongoGenre = new MongoGenre();
            mongoGenre.setId(stringOf(genre.getId()));
            mongoGenre.setName(genre.getName());
            mongoGenres.add(mongoGenre);
        }
        return mongoGenres;
    }

    private MongoAuthor getMongoAuthor(SqlBook book) {
        MongoAuthor mongoAuthor = new MongoAuthor();
        mongoAuthor.setId(stringOf(book.getAuthor().getId()));
        mongoAuthor.setFullName(book.getAuthor().getFullName());
        return mongoAuthor;
    }

    private String stringOf(Object value) {
        return String.valueOf(value);
    }

}
