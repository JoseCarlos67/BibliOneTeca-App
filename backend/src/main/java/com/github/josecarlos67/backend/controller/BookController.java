package com.github.josecarlos67.backend.controller;

import com.github.josecarlos67.backend.controller.dto.BookSaveRequestDTO;
import com.github.josecarlos67.backend.controller.dto.ErrorResponseDTO;
import com.github.josecarlos67.backend.execptions.DuplicateRecordException;
import com.github.josecarlos67.backend.model.collection.Book;
import com.github.josecarlos67.backend.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "books")
@RequiredArgsConstructor
public class BookController {

  private final BookService bookService;

  @GetMapping
  public ResponseEntity<List<Book>> search(
          @RequestParam(value = "title", required = false) String title,
          @RequestParam(value = "subtitle", required = false) String subtitle
  ) {
    List<Book> bookList = bookService.search(title, subtitle);

    return ResponseEntity.ok(bookList);
  }

  @PostMapping
  public ResponseEntity<Object> save(@RequestBody BookSaveRequestDTO bookSaveRequestDTO) {
    try {
      Book book = bookSaveRequestDTO.mapToBook();

      bookService.save(book);

      URI uri = ServletUriComponentsBuilder
              .fromCurrentRequest()
              .path("/{id}")
              .buildAndExpand(book.getId())
              .toUri();

      return ResponseEntity.created(uri).build();
    } catch (DuplicateRecordException e) {
      ErrorResponseDTO conflictDTO = ErrorResponseDTO.conflict(e.getMessage());

      return ResponseEntity.status(conflictDTO.status()).body(conflictDTO);
    }
  }

}
