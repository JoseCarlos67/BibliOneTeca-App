package com.github.josecarlos67.backend.model.collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.josecarlos67.backend.controller.dto.BookSaveRequestDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(name = "books")
@Data
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Setter(AccessLevel.NONE)
  private UUID id;

  private String title;
  private String subtitle;
  private String synopsis;

  @JsonIgnore
  @ManyToMany
  @JoinTable(
          name = "book_authors",
          joinColumns = @JoinColumn(name = "book_id"),
          inverseJoinColumns = @JoinColumn(name = "author_id")
  )
  private Set<Author> authors = new HashSet<>();

}
