package ru.otus.hw.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CreateOrEditBookDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    @GetMapping("/")
    public String getAllBooks(Model model) {
        model.addAttribute("books", bookService.findAll());
        List<BookDto> books = bookService.findAll().stream()
                .map(BookDto::toDto)
                .collect(Collectors.toList());
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/create-new-book")
    public String createNewBook(Model model) {
        model.addAttribute("book", new CreateOrEditBookDto());
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "save";
    }

    @GetMapping("/edit/{book_id}")
    public String editBook(@PathVariable long book_id, Model model) {
        Book b = bookService.findById(book_id).orElse(new Book());
        model.addAttribute("book", new CreateOrEditBookDto(b));
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "save";
    }

    @PostMapping("/save")
    public String saveBook(@ModelAttribute("book") CreateOrEditBookDto book) {
        bookService.update(book.getId(), book.getTitle(), book.getAuthorId(), Set.copyOf(book.getGenreIds()));
        return "redirect:/";
    }

    @GetMapping("/remove/{book_id}")
    public String removeBook(@PathVariable long book_id) {
        bookService.deleteById(book_id);
        return "redirect:/";
    }
}
