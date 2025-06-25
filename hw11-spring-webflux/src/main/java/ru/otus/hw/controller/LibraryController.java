package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CreateOrEditBookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class LibraryController {

    private final BookRepository bookRepository;

    private final BookConverter bookConverter;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @GetMapping("/api/books")
    public Flux<BookDto> allBooks() {
        return bookRepository.findAll().map(bookConverter::toDto);
    }

    @PostMapping("/api/books")
    @ResponseStatus(HttpStatus.OK)
    public Mono<BookDto> saveBook(@RequestBody @Valid CreateOrEditBookDto dto) {
        log.info("saving book: {} ", dto.toString());
        Mono<Author> a = authorRepository.findById(dto.getAuthorId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Author not found")));
        Mono<List<Genre>> g = genreRepository.findAllById(dto.getGenreIds())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Genres not found")))
                .collectList();
        Mono<Book> r = Mono.zip(a, g, (author, genres) -> new Book(dto.getId(), dto.getTitle(), author, genres));
        return r.flatMap(bookRepository::save).map(bookConverter::toDto);
    }

    @GetMapping("/api/books/{book_id}")
    public Mono<BookDto> bookById(@PathVariable(name = "book_id") String bookId) {
        return bookRepository.findById(bookId)
                .map(bookConverter::toDto)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Book not found")));
    }

    @DeleteMapping("/api/books/{book_id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> removeBook(@PathVariable(name = "book_id") String bookId) {
        log.info("deleting book id = {}", bookId);
        return bookRepository.deleteById(bookId);
    }

}
