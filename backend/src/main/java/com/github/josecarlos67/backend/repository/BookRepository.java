package com.github.josecarlos67.backend.repository;

import com.github.josecarlos67.backend.model.collection.Author;
import com.github.josecarlos67.backend.model.collection.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
  boolean existsByAuthors(Author author);
}
