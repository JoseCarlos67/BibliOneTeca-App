package com.github.josecarlos67.backend.controller.dto;

import com.github.josecarlos67.backend.model.collection.Book;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookSaveRequestDTO(
        @NotBlank(message = "Required field")
        @Size(max  = 255, message = "The title must contain a maximum of 255 characters")
        String title,

        @Size(max = 255, message ="The subtitle must contain a maximum of 255 characters")
        String subtitle,

        @NotBlank(message = "Required field")
        @Size(min = 150, message ="The subtitle must contain a minimum of 150 characters")
        String synopsis
) {

  public Book mapToBook() {
    Book book = new Book();

    book.setTitle(this.title);
    book.setSubtitle(this.subtitle);
    book.setSynopsis(this.synopsis);

    return book;
  }

}
