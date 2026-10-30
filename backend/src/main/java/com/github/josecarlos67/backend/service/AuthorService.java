package com.github.josecarlos67.backend.service;

import com.github.josecarlos67.backend.execptions.OperationNotPermittedException;
import com.github.josecarlos67.backend.model.collection.Author;
import com.github.josecarlos67.backend.model.enums.Gender;
import com.github.josecarlos67.backend.repository.AuthorRepository;
import com.github.josecarlos67.backend.validator.AuthorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorService {

  private final AuthorRepository authorRepository;
  private final AuthorValidator authorValidator;

  public Author save(Author author){
    authorValidator.validate(author);
    return authorRepository.save(author);
  }

  public void update(Author author) throws IllegalAccessException {
    if (author.getId() == null)
      throw new IllegalAccessException("To perform an update, the author must already be saved in the database.");

    authorValidator.validate(author);

    authorRepository.save(author);
  }

  public Optional<Author> getById(UUID id) {
    return  authorRepository.findById(id);
  }

  public void delete(Author author) {
    if (authorValidator.ownsBooks(author))
      throw new OperationNotPermittedException("It is not permitted to delete an author who has books registered!");

    authorRepository.delete(author);
  }

  public List<Author> search(String name, String nationality, Gender gender) {
    Author author = new Author();
    author.setName(name);
    author.setNationality(nationality);

    ExampleMatcher exampleMatcher = ExampleMatcher
            .matching()
            .withIgnoreNullValues()
            .withIgnoreCase()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example<Author> authorExample = Example.of(author, exampleMatcher);

    return authorRepository.findAll(authorExample);
  }

}
