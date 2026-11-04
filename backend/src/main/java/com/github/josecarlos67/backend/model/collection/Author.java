package com.github.josecarlos67.backend.model.collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.josecarlos67.backend.model.enums.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(name = "authors")
@Getter
@Setter
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
  @ManyToMany(mappedBy = "authors")
  private Set<Book> bookSet = new HashSet<>();

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Author author = (Author) o;

    return id != null && id.equals(author.id);
  }

  @Override
  public int  hashCode() {
    return getClass().hashCode();
  }

}
