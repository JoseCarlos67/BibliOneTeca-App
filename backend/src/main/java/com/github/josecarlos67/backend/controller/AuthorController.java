package com.github.josecarlos67.backend.controller;

import com.github.josecarlos67.backend.model.collection.Author;
import com.github.josecarlos67.backend.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/authors")
public class AuthorController {

  @Autowired
  private AuthorService authorService;

  @PostMapping
  public ResponseEntity<Object> saveAuthor(@RequestBody Author author){

    authorService.save(author);

    return ResponseEntity.status(HttpStatus.CREATED).build();

  }

}
