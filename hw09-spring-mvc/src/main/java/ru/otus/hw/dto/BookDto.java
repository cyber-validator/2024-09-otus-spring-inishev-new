package ru.otus.hw.dto;

import lombok.Data;

@Data
public class BookDto {

    private long id;

    private String title;

    private String author;

    private String genres;

}
