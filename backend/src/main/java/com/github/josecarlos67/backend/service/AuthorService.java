package com.github.josecarlos67.backend.service;

import com.github.josecarlos67.backend.model.enums.Gender;
import com.github.josecarlos67.backend.model.collection.Author;
import com.github.josecarlos67.backend.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorService {

  @Autowired
  private AuthorRepository authorRepository;

  public Author save(Author author){
    return authorRepository.save(author);
  }

  public void update(Author author) throws IllegalAccessException {
    if (author.getId() == null)
      throw new IllegalAccessException("To perform an update, the author must already be saved in the database.");

    authorRepository.save(author);
  }

  public Optional<Author> getById(UUID id) {
    return  authorRepository.findById(id);
  }

  public void delete(Author author) {
    authorRepository.delete(author);
  }

  public List<Author> search(String name, String nationality, Gender gender) {
    if(name != null && nationality != null && gender != null)
       return authorRepository.findByNameAndNationalityAndGender(name, nationality, gender);

    if(name != null)
      return authorRepository.findByName(name);

    if(nationality != null)
      return authorRepository.findByNationality(nationality);

    if(gender != null)
      return authorRepository.findByGender(gender);

    return authorRepository.findAll();
  }

}
