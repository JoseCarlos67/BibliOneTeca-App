package com.github.josecarlos67.backend.validator;

import com.github.josecarlos67.backend.execptions.DuplicateRecordException;
import com.github.josecarlos67.backend.model.collection.Author;
import com.github.josecarlos67.backend.model.collection.Book;
import com.github.josecarlos67.backend.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookValidator {

  private final BookRepository bookRepository;

  public void validate(Book book) {
    if (bookAlreadyRegistered(book))
      throw new DuplicateRecordException("Book already registered in the system!");
  }

  private boolean bookAlreadyRegistered(Book book) {
    return bookRepository.existsByTitleAndSubtitle(book.getTitle(), book.getSubtitle());
  }

}
