package ru.otus.hw.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controller.BookController;
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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldListAllBooks() throws Exception {
        Author author = new Author();
        author.setId(1);
        author.setFullName("Author 1");

        Genre genre = new Genre();
        genre.setId(1);
        genre.setName("Genre 1");

        Book book = new Book();
        book.setTitle("Book Title");
        book.setAuthor(author);
        book.setGenres(List.of(genre));

        Book book2 = new Book();
        book2.setTitle("Book 2 Title");
        book2.setAuthor(author);
        book2.setGenres(List.of(genre));

        when(bookService.findAll()).thenReturn(List.of(book, book2));
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", List.of(BookDto.toDto(book), BookDto.toDto(book2))));
    }

    @Test
    void shouldOpenBookCreatingPage() throws Exception {
        Author author = new Author();
        author.setId(1);
        author.setFullName("Author 1");

        Genre genre = new Genre();
        genre.setId(1);
        genre.setName("Genre 1");

        when(authorService.findAll()).thenReturn(List.of(author));
        when(genreService.findAll()).thenReturn(List.of(genre));

        mvc.perform(get("/create-new-book"))
                .andExpect(status().isOk())
                .andExpect(view().name("save"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("authors", List.of(author)))
                .andExpect(model().attribute("genres", List.of(genre)));
    }

    @Test
    void shouldSaveEditedBook() throws Exception {
        Author author = new Author();
        author.setId(1);
        author.setFullName("Author 1");

        Genre genre = new Genre();
        genre.setId(1);
        genre.setName("Genre 1");

        Book original = new Book();
        original.setTitle("Book Title");
        original.setAuthor(author);
        original.setGenres(List.of(genre));

        Book modified = new Book();
        modified.setTitle("Edited Book Title");
        modified.setAuthor(author);
        modified.setGenres(List.of(genre));

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
                .update(modified.getId(), modified.getTitle(), modified.getAuthor().getId(), Set.of(genre.getId()));
    }

    @Test
    void shouldCreateNewBook() throws Exception {
        Author author = new Author();
        author.setId(1);
        author.setFullName("Author 1");

        Genre genre = new Genre();
        genre.setId(1);
        genre.setName("Genre 1");

        Book book = new Book();
        book.setTitle("Created Book Title");
        book.setAuthor(author);
        book.setGenres(List.of(genre));

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
                .update(book.getId(), book.getTitle(), book.getAuthor().getId(), Set.of(genre.getId()));
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
        Author author = new Author();
        author.setId(1);
        author.setFullName("Author 1");

        Genre genre = new Genre();
        genre.setId(1);
        genre.setName("Genre 1");

        Book book = new Book();
        book.setTitle("Created Book Title");
        book.setAuthor(author);
        book.setGenres(List.of(genre));

        when(authorService.findAll()).thenReturn(List.of(author));
        when(genreService.findAll()).thenReturn(List.of(genre));

        mvc.perform(post("/save")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("id=" + book.getId() +
                                "&title="  +
                                "&authorId=" + book.getAuthor().getId() +
                                "&genreIds=" + book.getGenres().get(0).getId() +
                                "&_genreIds=on&_genreIds=on&_genreIds=on" +
                                "&_genreIds=on&_genreIds=on&_genreIds=on"))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("save"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("authors", List.of(author)))
                .andExpect(model().attribute("genres", List.of(genre)));

        verify(bookService, times(0))
                .update(book.getId(), book.getTitle(), book.getAuthor().getId(), Set.of(genre.getId()));
    }

}