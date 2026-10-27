package com.github.josecarlos67.backend.controller.dto;

import com.github.josecarlos67.backend.model.collection.Author;

import java.time.LocalDate;

public record AuthorDTO(
          String name,
          LocalDate dateOfBirth,
          LocalDate dateOfDeath,
          String nationality
) {

  public Author mapToAuthor() {
    Author author = new Author();

    author.setName(this.name);
    author.setDateOfBirth(this.dateOfBirth);
    author.setDateOfDeath(this.dateOfDeath);
    author.setNationality(this.nationality);

    return author;
  }

}
