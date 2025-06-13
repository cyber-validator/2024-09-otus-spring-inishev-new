package ru.otus.hw.actuator;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.services.BookService;

@RequiredArgsConstructor
@Component("libraryStorage")
public class LibraryHealthIndicator implements HealthIndicator {

    private final BookService bookService;

    @Override
    public Health health() {
        long booksAvailable = bookService.countBooks();
        Health.Builder status = booksAvailable > 0 ? Health.up() : Health.down();
        return status.withDetail("books_available", booksAvailable).build();
    }

}
