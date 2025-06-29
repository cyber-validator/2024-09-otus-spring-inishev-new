package ru.otus.hw.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ChangeLog
public class InitDataBaseChangeLog {

    private final Map<Integer, Genre> genres = new HashMap<>();

    private final Map<Integer, Author> authors = new HashMap<>();

    private final Map<Integer, Book> books = new HashMap<>();

    @ChangeSet(order = "001", id = "dropDb", author = "Otus student", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "Otus student")
    public void insertAuthors(AuthorRepository authorRepository) {
        authors.put(1, authorRepository.save(new Author("1", "Author_1")).block());
        authors.put(2, authorRepository.save(new Author("2", "Author_2")).block());
        authors.put(3, authorRepository.save(new Author("3", "Author_3")).block());

    }

    @ChangeSet(order = "003", id = "insertGenres", author = "Otus student")
    public void insertGenres(GenreRepository genreRepository) {
        genres.put(1, genreRepository.save(new Genre("1", "Genre_1")).block());
        genres.put(2, genreRepository.save(new Genre("2", "Genre_2")).block());
        genres.put(3, genreRepository.save(new Genre("3", "Genre_3")).block());
        genres.put(4, genreRepository.save(new Genre("4", "Genre_4")).block());
        genres.put(5, genreRepository.save(new Genre("5", "Genre_5")).block());
        genres.put(6, genreRepository.save(new Genre("6", "Genre_6")).block());
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "Otus student")
    public void insertBooks(BookRepository bookRepository) {
        books.put(1, bookRepository.save(new Book("1", "BookTitle_1", authors.get(1),
                List.of(genres.get(1), genres.get(2)))).block());
        books.put(2, bookRepository.save(new Book("2", "BookTitle_2", authors.get(2),
                List.of(genres.get(3), genres.get(4)))).block());
        books.put(3, bookRepository.save(new Book("3", "BookTitle_3", authors.get(3),
                List.of(genres.get(5), genres.get(6)))).block());
    }

}