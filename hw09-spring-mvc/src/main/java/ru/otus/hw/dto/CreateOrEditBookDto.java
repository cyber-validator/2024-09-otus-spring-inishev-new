package ru.otus.hw.dto;

import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.ArrayList;
import java.util.List;

public class CreateOrEditBookDto {
    private long id;
    private String title;
    private long authorId;
    private List<Long> genreIds;

    public CreateOrEditBookDto() {
    }

    public CreateOrEditBookDto(Book b) {
        this.id = b.getId();
        this.title = b.getTitle();
        this.authorId = b.getAuthor().getId();
//        this.authorId = String.valueOf(b.getAuthor().getId());
        this.genreIds = new ArrayList<>();
        for (Genre g : b.getGenres()) {
            this.genreIds.add(g.getId());
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public List<Long> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Long> genreIds) {
        this.genreIds = genreIds;
    }

}
