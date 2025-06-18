package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.models.Book;

@RepositoryRestResource(path = "books")
public interface BookRepository extends JpaRepository<Book, Long> {

}
