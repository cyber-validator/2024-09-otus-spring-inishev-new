package ru.otus.hw.converter;

import org.springframework.stereotype.Service;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.stream.Collectors;

@Service
public class BookConverter {

    public BookDto toDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor().getFullName());
        bookDto.setGenres(book.getGenres().stream().map(Genre::getName).collect(Collectors.joining(", ")));
        return bookDto;
    }

}
