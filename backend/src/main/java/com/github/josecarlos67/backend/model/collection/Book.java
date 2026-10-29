package com.github.josecarlos67.backend.model.collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

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

  @JsonIgnore
  @ManyToMany
  @JoinTable(
          name = "book_authors",
          joinColumns = @JoinColumn(name = "book_id"),
          inverseJoinColumns = @JoinColumn(name = "author_id")
  )
  private Set<Author> authorSet = new HashSet<>();

}
