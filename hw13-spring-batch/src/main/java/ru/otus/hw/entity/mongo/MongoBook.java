package ru.otus.hw.entity.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "books")
public class MongoBook {

    @Id
    private String id;

    private String title;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private MongoAuthor author;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<MongoGenre> genres;

}
