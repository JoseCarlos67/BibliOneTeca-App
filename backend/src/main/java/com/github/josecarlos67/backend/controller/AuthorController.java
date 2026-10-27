package com.github.josecarlos67.backend.controller;

import com.github.josecarlos67.backend.controller.dto.AuthorRequestDTO;
import com.github.josecarlos67.backend.controller.dto.AuthorResponseDTO;
import com.github.josecarlos67.backend.model.collection.Author;
import com.github.josecarlos67.backend.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "authors")
public class AuthorController {

  @Autowired
  private AuthorService authorService;

  @PostMapping
  public ResponseEntity<Void> save(@RequestBody AuthorRequestDTO authorRequestDTO){
    Author author = authorRequestDTO.mapToAuthor();

    authorService.save(author);

    URI uri = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(author.getId())
            .toUri();

    return ResponseEntity.created(uri).build();
  }

  @GetMapping("{id}")
  public ResponseEntity<AuthorResponseDTO> getDetails(@PathVariable UUID id) {
    Optional<Author> authorOptional = authorService.getById(id);

    if(authorOptional.isPresent()) {
      Author author = authorOptional.get();

      AuthorResponseDTO authorResponseDTO = new AuthorResponseDTO(
              author.getId(),
              author.getName(),
              author.getDateOfBirth(),
              author.getDateOfDeath(),
              author.getNationality()
      );

      return ResponseEntity.ok(authorResponseDTO);
    }

    return ResponseEntity.notFound().build();
  }

}
