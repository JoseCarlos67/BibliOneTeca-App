package com.github.josecarlos67.backend.controller.dto;

import com.github.josecarlos67.backend.model.enums.Gender;
import com.github.josecarlos67.backend.model.collection.Author;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record AuthorRequestDTO(
        @NotBlank(message = "Required field")
        @Size(max = 255, message = "The name must contain a maximum of 255 characters!")
        String name,

        @NotNull(message = "Required field")
        @Past(message = "The date of birth cannot bea a future date!")
        LocalDate dateOfBirth,

        @PastOrPresent(message = "The date of death cannot bea a future date!")
        LocalDate dateOfDeath,

        @NotBlank(message = "Required field")
        @Size(max = 100, message = "The nationality must contain a maximum of 100 characters!")
        String nationality,

        @NotNull(message = "Required field")
        Gender gender
) {

  public Author mapToAuthor() {
    Author author = new Author();

    author.setName(this.name);
    author.setDateOfBirth(this.dateOfBirth);
    author.setDateOfDeath(this.dateOfDeath);
    author.setNationality(this.nationality);
    author.setGender(this.gender);

    return author;
  }

}
