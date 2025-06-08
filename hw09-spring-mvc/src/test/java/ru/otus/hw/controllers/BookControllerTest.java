package ru.otus.hw.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controller.BookController;
import ru.otus.hw.converter.BookConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = BookController.class)
@Import(BookConverter.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookConverter bookConverter;

    @MockBean
    private BookService bookService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;

    private List<Author> authors;

    private List<Genre> genres;

    @BeforeEach
    void setUp() {
        authors = getAuthors();
        genres = getGenres();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldListAllBooks() throws Exception {
        Book book = createBook("Book Title 1", authors.get(0), genres);
        Book book2 = createBook("Book Title 2", authors.get(0), List.of(genres.get(0)));

        when(bookService.findAll()).thenReturn(List.of(book, book2));
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books",
                        List.of(bookConverter.toDto(book), bookConverter.toDto(book2))));
    }

    @Test
    void shouldOpenBookCreatingPage() throws Exception {
        when(authorService.findAll()).thenReturn(authors);
        when(genreService.findAll()).thenReturn(genres);

        mvc.perform(get("/create-new-book"))
                .andExpect(status().isOk())
                .andExpect(view().name("save"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("authors", authors))
                .andExpect(model().attribute("genres", genres));
    }

    @Test
    void shouldSaveEditedBook() throws Exception {
        Book original = createBook("Book Title Original", authors.get(0), List.of(genres.get(0)));
        Book modified = createBook("Book Title Edited", authors.get(1), List.of(genres.get(0)));

        when(bookService.findById(1L)).thenReturn(Optional.of(original));
        when(bookService.findAll()).thenReturn(List.of(modified));

        mvc.perform(post("/save")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("id=" + modified.getId() +
                                "&title=" + modified.getTitle() +
                                "&authorId=" + modified.getAuthor().getId() +
                                "&genreIds=" + modified.getGenres().get(0).getId() +
                                "&_genreIds=on&_genreIds=on&_genreIds=on" +
                                "&_genreIds=on&_genreIds=on&_genreIds=on"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(bookService, times(1))
                .update(modified.getId(),
                        modified.getTitle(),
                        modified.getAuthor().getId(),
                        Set.of(modified.getGenres().get(0).getId()));
    }

    @Test
    void shouldCreateNewBook() throws Exception {
        Book book = createBook("Created Book Title", authors.get(2), List.of(genres.get(0)));

        mvc.perform(post("/save")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("id=" + book.getId() +
                                "&title=" + book.getTitle() +
                                "&authorId=" + book.getAuthor().getId() +
                                "&genreIds=" + book.getGenres().get(0).getId() +
                                "&_genreIds=on&_genreIds=on&_genreIds=on" +
                                "&_genreIds=on&_genreIds=on&_genreIds=on"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(bookService, times(1))
                .update(book.getId(),
                        book.getTitle(),
                        book.getAuthor().getId(),
                        Set.of(book.getGenres().get(0).getId()));
    }

    @Test
    void removeBook() throws Exception {
        long id = 1L;
        mvc.perform(get(String.format("/remove/%d", id)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(bookService, times(1)).deleteById(id);
    }

    @Test
    void shouldReturnValidationErrorsOnCreateNewBookWithEmptyTitle() throws Exception {
        Book book = createBook("Created Book Title", authors.get(0), List.of(genres.get(0)));

        when(authorService.findAll()).thenReturn(authors);
        when(genreService.findAll()).thenReturn(genres);

        mvc.perform(post("/save")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("id=" + book.getId() +
                                "&title=" +
                                "&authorId=" + book.getAuthor().getId() +
                                "&genreIds=" + book.getGenres().get(0).getId() +
                                "&_genreIds=on&_genreIds=on&_genreIds=on" +
                                "&_genreIds=on&_genreIds=on&_genreIds=on"))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("save"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("authors", authors))
                .andExpect(model().attribute("genres", genres));

        verify(bookService, times(0))
                .update(book.getId(),
                        book.getTitle(),
                        book.getAuthor().getId(),
                        Set.of(book.getGenres().get(0).getId()));
    }

    private static List<Author> getAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private Book createBook(String title, Author author, List<Genre> genres) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenres(genres);
        return book;
    }

}