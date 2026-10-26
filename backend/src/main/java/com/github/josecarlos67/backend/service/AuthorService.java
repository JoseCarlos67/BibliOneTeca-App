package com.github.josecarlos67.backend.service;

import com.github.josecarlos67.backend.model.collection.Author;
import com.github.josecarlos67.backend.model.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

  @Autowired
  private AuthorRepository authorRepository;

  public Author save(Author author){

    return authorRepository.save(author);

  }

}
