package ru.otus.hw.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.converter.AuthorConverter;
import ru.otus.hw.converter.BookConverter;
import ru.otus.hw.converter.GenreConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CreateOrEditBookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class BookLibraryManagerDemoController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final BookConverter bookConverter;

    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    @GetMapping("/api/authors")
    public List<AuthorDto> getAllAuthors() {
        return authorService.findAll().stream().map(authorConverter::toDto).toList();
    }

    @GetMapping("/api/genres")
    public List<GenreDto> getAllGenres() {
        return genreService.findAll().stream().map(genreConverter::toDto).toList();
    }

    @GetMapping("/api/books")
    public List<BookDto> getAllBooks() {
        return bookService.findAll().stream().map(bookConverter::toDto).toList();
    }

    @PostMapping("/api/books")
    @ResponseStatus(HttpStatus.OK)
    public void saveBook(@RequestBody CreateOrEditBookDto book) throws JsonProcessingException {
        bookService.update(book.getId(), book.getTitle(), book.getAuthorId(), Set.copyOf(book.getGenreIds()));
    }

    @GetMapping("/api/books/{book_id}")
    public BookDto getBookById(@PathVariable(name = "book_id") long bookId) {
        Book b = bookService.findById(bookId).orElse(new Book());
        return bookConverter.toDto(b);
    }

    @DeleteMapping("/api/books/{book_id}")
    @ResponseStatus(HttpStatus.OK)
    public void removeBook(@PathVariable(name = "book_id") long bookId) {
        bookService.deleteById(bookId);
    }

}
