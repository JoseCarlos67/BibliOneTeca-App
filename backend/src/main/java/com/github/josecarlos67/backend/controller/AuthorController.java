package com.github.josecarlos67.backend.controller;

import com.github.josecarlos67.backend.controller.dto.AuthorDTO;
import com.github.josecarlos67.backend.model.collection.Author;
import com.github.josecarlos67.backend.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "authors")
public class AuthorController {

  @Autowired
  private AuthorService authorService;

  @PostMapping
  public ResponseEntity<Object> save(@RequestBody AuthorDTO authorDTO){
    Author author = authorDTO.mapToAuthor();

    authorService.save(author);

    URI uri = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(author.getId())
            .toUri();

    return ResponseEntity.created(uri).build();
  }

}
