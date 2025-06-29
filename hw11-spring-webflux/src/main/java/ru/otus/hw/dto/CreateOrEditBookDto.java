package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrEditBookDto {

    private String id;

    @NotBlank(message = "The title can't be empty")
    private String title;

    private String authorId;

    @NotEmpty(message = "Need to select at least one genre")
    private List<String> genreIds;

}
