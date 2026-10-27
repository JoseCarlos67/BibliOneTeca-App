package com.github.josecarlos67.backend.service;

import com.github.josecarlos67.backend.model.collection.Author;
import com.github.josecarlos67.backend.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorService {

  @Autowired
  private AuthorRepository authorRepository;

  public void save(Author author){
    authorRepository.save(author);
  }

  public Optional<Author> getById(UUID id) {
    return  authorRepository.findById(id);
  }

}
