package com.github.josecarlos67.backend.controller;

import com.github.josecarlos67.backend.controller.dto.AuthorRequestDTO;
import com.github.josecarlos67.backend.controller.dto.AuthorResponseDTO;
import com.github.josecarlos67.backend.model.Gender;
import com.github.josecarlos67.backend.model.collection.Author;
import com.github.josecarlos67.backend.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "authors")
public class AuthorController {

  @Autowired
  private AuthorService authorService;

  @PostMapping
  public ResponseEntity<Void> save(@RequestBody AuthorRequestDTO authorRequestDTO) {
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
  public ResponseEntity<AuthorResponseDTO> getDetailsById(@PathVariable UUID id) {
    Optional<Author> authorOptional = authorService.getById(id);

    if (authorOptional.isPresent()) {
      Author author = authorOptional.get();

      AuthorResponseDTO authorResponseDTO = new AuthorResponseDTO(
              author.getId(),
              author.getName(),
              author.getDateOfBirth(),
              author.getDateOfDeath(),
              author.getNationality(),
              author.getGender()
      );

      return ResponseEntity.ok(authorResponseDTO);
    }

    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    Optional<Author> authorOptional = authorService.getById(id);

    if (authorOptional.isEmpty())
      return ResponseEntity.notFound().build();

    authorService.delete(authorOptional.get());

    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<List<AuthorResponseDTO>> search(
          @RequestParam(value = "name", required = false) String name,
          @RequestParam(value = "nationality", required = false) String nationality,
          @RequestParam(value = "gender", required = false) Gender gender
  ) {
    List<Author> authorList = authorService.search(name, nationality, gender);

    List<AuthorResponseDTO> authorResponseDTOList = authorList
            .stream()
            .map(author -> new AuthorResponseDTO(
                    author.getId(),
                    author.getName(),
                    author.getDateOfBirth(),
                    author.getDateOfDeath(),
                    author.getNationality(),
                    author.getGender()))
            .toList();

    return ResponseEntity.ok(authorResponseDTOList);
  }

  @PutMapping("{id}")
  public ResponseEntity<Void> update(
          @PathVariable UUID id,
          @RequestBody AuthorRequestDTO authorRequestDTO
  ) throws IllegalAccessException {
    Optional<Author> authorOptional = authorService.getById(id);

    if (authorOptional.isEmpty())
      return ResponseEntity.notFound().build();

    Author author = authorOptional.get();
    author.setName(authorRequestDTO.name());
    author.setDateOfBirth(authorRequestDTO.dateOfBirth());
    author.setDateOfBirth(authorRequestDTO.dateOfDeath());
    author.setNationality(authorRequestDTO.nationality());

    authorService.update(author);

    return ResponseEntity.noContent().build();
  }

}
