package com.github.josecarlos67.backend.validator;

import com.github.josecarlos67.backend.execptions.DuplicateRecordException;
import com.github.josecarlos67.backend.model.collection.Author;
import com.github.josecarlos67.backend.repository.AuthorRepository;
import com.github.josecarlos67.backend.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorValidator {

  private final AuthorRepository authorRepository;
  private final BookRepository bookRepository;

  public void validate(Author author) {
    if (authorAlreadyRegistered(author)) {
      throw new DuplicateRecordException("Author already registered in the system!");
    }
  }

  public boolean ownsBooks(Author author ) {
    return bookRepository.existsByAuthors(author);
  }

  private boolean authorAlreadyRegistered(Author author) {
    Optional<Author> authorOptional = authorRepository.findByNameAndDateOfBirthAndNationality(
            author.getName(),
            author.getDateOfBirth(),
            author.getNationality()
    );

    if (authorOptional.isEmpty()) {
      return false;
    }

    Author existingAuthor = authorOptional.get();

    if (author.getId() == null) {
      return true;
    }

    return !author.getId().equals(existingAuthor.getId());
  }

}