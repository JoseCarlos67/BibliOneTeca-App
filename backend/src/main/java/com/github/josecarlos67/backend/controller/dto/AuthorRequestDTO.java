package com.github.josecarlos67.backend.controller.dto;

import com.github.josecarlos67.backend.model.enums.Gender;
import com.github.josecarlos67.backend.model.collection.Author;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AuthorRequestDTO(
        @NotBlank(message = "Required field")
        String name,

        @NotNull(message = "Required field")
        LocalDate dateOfBirth,

        LocalDate dateOfDeath,

        @NotBlank(message = "Required field")
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
