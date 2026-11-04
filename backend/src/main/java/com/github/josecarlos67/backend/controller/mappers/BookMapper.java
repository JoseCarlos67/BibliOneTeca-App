package com.github.josecarlos67.backend.controller.mappers;

import com.github.josecarlos67.backend.controller.dto.BookSaveRequestDTO;
import com.github.josecarlos67.backend.model.collection.Author;
import com.github.josecarlos67.backend.model.collection.Book;
import com.github.josecarlos67.backend.repository.AuthorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class BookMapper {

  @Autowired
  AuthorRepository authorRepository;

  @Mapping(target = "authors", source = "authors")
  public abstract Book toEntity(BookSaveRequestDTO dto);

  protected Set<Author> mapAuthors(Set<UUID> authorsId) {
    if (authorsId == null || authorsId.isEmpty())
      return new HashSet<>();

    return new HashSet<>(authorRepository.findAllById(authorsId));
  }

}
