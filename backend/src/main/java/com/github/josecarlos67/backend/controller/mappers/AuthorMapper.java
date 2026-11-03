package com.github.josecarlos67.backend.controller.mappers;

import com.github.josecarlos67.backend.controller.dto.AuthorRequestDTO;
import com.github.josecarlos67.backend.controller.dto.AuthorResponseDTO;
import com.github.josecarlos67.backend.model.collection.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

  Author toEntity(AuthorRequestDTO dto);

  AuthorResponseDTO toDTO(Author entity);

}
