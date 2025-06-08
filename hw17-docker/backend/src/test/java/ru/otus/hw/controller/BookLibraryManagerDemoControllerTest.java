package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.converter.AuthorConverter;
import ru.otus.hw.converter.BookConverter;
import ru.otus.hw.converter.GenreConverter;
import ru.otus.hw.dto.CreateOrEditBookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({BookLibraryManagerDemoController.class})
@Import({BookConverter.class, AuthorConverter.class, GenreConverter.class})
public class BookLibraryManagerDemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    private final long authorId = 1L;
    private final long bookId = 2L;
    private final long genreId = 3L;
    private final String authorFullName = "Author name 1";
    private final String bookTitle = "Book title 1";
    private final String genreName = "Genre name 1";

    @Test
    public void getAllAuthors() throws Exception {
        when(authorService.findAll()).thenReturn(List.of(getAuthor()));

        mockMvc.perform(get("/api/authors"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(authorId))
                .andExpect(jsonPath("$[0].fullName").value(authorFullName));
    }

    @Test
    public void getAllGenres() throws Exception {
        when(genreService.findAll()).thenReturn(List.of(getGenre()));

        mockMvc.perform(get("/api/genres"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(genreId))
                .andExpect(jsonPath("$[0].name").value(genreName));
    }

    @Test
    public void getAllBooks() throws Exception {
        when(bookService.findAll()).thenReturn(List.of(getBook()));

        mockMvc.perform(get("/api/books"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookId))
                .andExpect(jsonPath("$[0].author.id").value(authorId))
                .andExpect(jsonPath("$[0].title").value(bookTitle));
    }

    @Test
    public void saveBook() throws Exception {
        CreateOrEditBookDto dto = getCreateOrEditBookDto();
        String json = new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(dto);

        mockMvc.perform(post("/api/books")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andDo(print());
        verify(bookService, times(1))
                .update(dto.getId(), dto.getTitle(), dto.getAuthorId(), Set.copyOf(dto.getGenreIds()));
    }

    @Test
    public void getBookById() throws Exception {
        when(bookService.findById(bookId)).thenReturn(Optional.of(getBook()));

        mockMvc.perform(get("/api/books/{0}", bookId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(bookId))
                .andExpect(jsonPath("author.id").value(authorId))
                .andExpect(jsonPath("title").value(bookTitle));
    }

    @Test
    public void removeBook() throws Exception {
        mockMvc.perform(delete("/api/books/{0}", bookId))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andDo(print());
        verify(bookService, times(1)).deleteById(bookId);
    }

    private Author getAuthor() {
        Author author = new Author();
        author.setId(authorId);
        author.setFullName(authorFullName);
        return author;
    }

    private Genre getGenre() {
        Genre genre = new Genre();
        genre.setId(genreId);
        genre.setName(genreName);
        return genre;
    }

    private Book getBook() {
        Book book = new Book();
        book.setId(bookId);
        book.setAuthor(getAuthor());
        book.setTitle(bookTitle);
        book.setGenres(List.of(getGenre()));
        return book;
    }

    private CreateOrEditBookDto getCreateOrEditBookDto() {
        CreateOrEditBookDto dto = new CreateOrEditBookDto();
        dto.setId(bookId);
        dto.setAuthorId(getAuthor().getId());
        dto.setTitle(bookTitle);
        dto.setGenreIds(List.of(getGenre().getId()));
        return dto;
    }

}
