package ru.otus.hw.converters;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookConverter {

    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    public BookDto toDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(authorConverter.toDto(book.getAuthor()));
        bookDto.setGenres(book.getGenres().stream().map(genreConverter::toDto).collect(Collectors.toList()));
        return bookDto;
    }

}
