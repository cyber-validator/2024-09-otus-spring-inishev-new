package ru.otus.hw.converter;

import org.springframework.stereotype.Component;
import ru.otus.hw.entity.jpa.JpaBook;
import ru.otus.hw.entity.jpa.JpaGenre;
import ru.otus.hw.entity.mongo.MongoAuthor;
import ru.otus.hw.entity.mongo.MongoBook;
import ru.otus.hw.entity.mongo.MongoGenre;

import java.util.ArrayList;
import java.util.List;

@Component
public class JpaToMongoBookConverter extends ValueToStringConverter {

    public MongoBook convert(JpaBook book) {
        MongoAuthor mongoAuthor = getMongoAuthor(book);
        List<MongoGenre> mongoGenres = getMongoGenres(book);
        return getMongoBook(book, mongoAuthor, mongoGenres);
    }

    private MongoBook getMongoBook(JpaBook book, MongoAuthor mongoAuthor, List<MongoGenre> mongoGenres) {
        MongoBook mongoBook = new MongoBook();
        mongoBook.setId(stringOf(book.getId()));
        mongoBook.setTitle(book.getTitle());
        mongoBook.setAuthor(mongoAuthor);
        mongoBook.setGenres(mongoGenres);
        return mongoBook;
    }

    private List<MongoGenre> getMongoGenres(JpaBook book) {
        List<MongoGenre> mongoGenres = new ArrayList<>();
        for (JpaGenre genre: book.getGenres()) {
            MongoGenre mongoGenre = new MongoGenre();
            mongoGenre.setId(stringOf(genre.getId()));
            mongoGenre.setName(genre.getName());
            mongoGenres.add(mongoGenre);
        }
        return mongoGenres;
    }

    private MongoAuthor getMongoAuthor(JpaBook book) {
        MongoAuthor mongoAuthor = new MongoAuthor();
        mongoAuthor.setId(stringOf(book.getAuthor().getId()));
        mongoAuthor.setFullName(book.getAuthor().getFullName());
        return mongoAuthor;
    }

}
