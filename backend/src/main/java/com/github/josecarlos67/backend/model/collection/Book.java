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
  private String subtitle;
  private String synopsis;
  private String isbn;

  @Column(name = "edition_number")
  private Integer editionNumber;

  @Column(name = "year_publication")
  private Integer yearPublication;

  private String language;

  @Column(name = "page_number")
  private Integer pageNumber;

  @Column(name = "cover_url")
  private String coverUrl;

  @JsonIgnore
  @ManyToMany
  @JoinTable(
          name = "book_authors",
          joinColumns = @JoinColumn(name = "book_id"),
          inverseJoinColumns = @JoinColumn(name = "author_id")
  )
  private Set<Author> authors = new HashSet<>();

  @Column(name = "publisher_id")
  private UUID publisherId;

}
