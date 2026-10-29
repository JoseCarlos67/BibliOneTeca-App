package com.github.josecarlos67.backend.model.collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.josecarlos67.backend.model.enums.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(name = "authors")
@Data
public class Author {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Setter(AccessLevel.NONE)
  private UUID id;

  private String name;

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

  @Column(name = "date_of_death")
  private LocalDate dateOfDeath;

  private String nationality;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  @JsonIgnore
  @ManyToMany(mappedBy = "authorSet")
  private Set<Book> bookSet = new HashSet<>();

}
