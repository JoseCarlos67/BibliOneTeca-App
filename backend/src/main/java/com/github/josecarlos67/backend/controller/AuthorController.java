package com.github.josecarlos67.backend.controller;

import com.github.josecarlos67.backend.controller.dto.AuthorRequestDTO;
import com.github.josecarlos67.backend.controller.dto.AuthorResponseDTO;
import com.github.josecarlos67.backend.controller.dto.ErrorResponseDTO;
import com.github.josecarlos67.backend.execptions.DuplicateRecordException;
import com.github.josecarlos67.backend.execptions.OperationNotPermittedException;
import com.github.josecarlos67.backend.model.enums.Gender;
import com.github.josecarlos67.backend.model.collection.Author;
import com.github.josecarlos67.backend.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class AuthorController {

  private final AuthorService authorService;

  @PostMapping
  public ResponseEntity<Object> save(@RequestBody @Valid AuthorRequestDTO authorRequestDTO) {
    try {
      Author author = authorRequestDTO.mapToAuthor();

      authorService.save(author);

      URI uri = ServletUriComponentsBuilder
              .fromCurrentRequest()
              .path("/{id}")
              .buildAndExpand(author.getId())
              .toUri();

      return ResponseEntity.created(uri).build();
    } catch (DuplicateRecordException e) {
      ErrorResponseDTO conflictDto = ErrorResponseDTO.conflict(e.getMessage());
      return ResponseEntity.status(conflictDto.status()).body(conflictDto);
    }
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
  public ResponseEntity<Object> delete(@PathVariable UUID id) {
    try {
      Optional<Author> authorOptional = authorService.getById(id);

      if (authorOptional.isEmpty())
        return ResponseEntity.notFound().build();

      authorService.delete(authorOptional.get());

      return ResponseEntity.noContent().build();
    } catch (OperationNotPermittedException e) {
      ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.standardError(e.getMessage());
      return ResponseEntity.status(errorResponseDTO.status()).body(errorResponseDTO);
    }
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
  public ResponseEntity<Object> update(
          @PathVariable UUID id,
          @RequestBody @Valid AuthorRequestDTO authorRequestDTO
  ) throws IllegalAccessException {
    try {
      Optional<Author> authorOptional = authorService.getById(id);

      if (authorOptional.isEmpty())
        return ResponseEntity.notFound().build();

      Author author = authorOptional.get();
      author.setName(authorRequestDTO.name());
      author.setDateOfBirth(authorRequestDTO.dateOfBirth());
      author.setDateOfBirth(authorRequestDTO.dateOfDeath());
      author.setNationality(authorRequestDTO.nationality());
      author.setGender(authorRequestDTO.gender());

      authorService.update(author);

      return ResponseEntity.noContent().build();
    } catch (DuplicateRecordException e) {
      ErrorResponseDTO conflictDto = ErrorResponseDTO.conflict(e.getMessage());
      return ResponseEntity.status(conflictDto.status()).body(conflictDto);
    }
  }

}
