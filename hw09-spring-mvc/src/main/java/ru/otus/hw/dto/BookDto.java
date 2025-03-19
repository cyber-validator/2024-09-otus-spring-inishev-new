package ru.otus.hw.dto;

import lombok.Data;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.stream.Collectors;

@Data
public class BookDto {

    private long id;

    private String title;

    private String author;

    private String genres;

    public static BookDto toDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor().getFullName());
        bookDto.setGenres(book.getGenres().stream().map(Genre::getName).collect(Collectors.joining(", ")));
        return bookDto;
    }

}
