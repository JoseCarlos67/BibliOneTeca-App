package com.github.josecarlos67.backend.model.collection;

import com.github.josecarlos67.backend.model.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;
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

}
