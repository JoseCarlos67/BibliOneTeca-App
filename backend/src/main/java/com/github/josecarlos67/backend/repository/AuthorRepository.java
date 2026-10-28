package com.github.josecarlos67.backend.repository;

import com.github.josecarlos67.backend.model.enums.Gender;
import com.github.josecarlos67.backend.model.collection.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID> {

  List<Author> findByName(String name);
  List<Author> findByNationality(String nationality);
  List<Author> findByGender(Gender gender);
  List<Author> findByNameAndNationalityAndGender(String name, String nationality, Gender gender);

}
