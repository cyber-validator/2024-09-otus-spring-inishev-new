package ru.otus.hw.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.services.ExtRestService;

@RestController
@AllArgsConstructor
public class BookController {

    private final ExtRestService service;

    @GetMapping("/")
    public String getAllBooks() {
        return service.getBooks();
    }

}
