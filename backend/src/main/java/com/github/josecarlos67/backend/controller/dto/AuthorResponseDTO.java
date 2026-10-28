package com.github.josecarlos67.backend.controller.dto;

import com.github.josecarlos67.backend.model.enums.Gender;

import java.time.LocalDate;
import java.util.UUID;

public record AuthorResponseDTO(
        UUID id,
        String name,
        LocalDate dateOfBirth,
        LocalDate dateOfDeath,
        String nationality,
        Gender gender
) {
}
