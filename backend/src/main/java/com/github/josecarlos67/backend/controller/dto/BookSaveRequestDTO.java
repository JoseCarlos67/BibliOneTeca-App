package com.github.josecarlos67.backend.controller.dto;

import com.github.josecarlos67.backend.model.collection.Book;

public record BookSaveRequestDTO(
  String title,
  String subtitle,
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
