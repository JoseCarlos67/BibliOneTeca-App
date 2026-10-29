package com.github.josecarlos67.backend.controller;

import com.github.josecarlos67.backend.model.collection.Book;
import com.github.josecarlos67.backend.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "books")
public class BookController {

  @Autowired
  private BookRepository bookRepository;

  @GetMapping
  public ResponseEntity<List<Book>> findAll() {
    return ResponseEntity.ok(bookRepository.findAll());
  }

}
