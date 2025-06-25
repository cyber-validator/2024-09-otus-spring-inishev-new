package ru.otus.hw.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookDto {

    private String id;

    private String title;

    private AuthorDto author;

    private List<GenreDto> genres;

}
