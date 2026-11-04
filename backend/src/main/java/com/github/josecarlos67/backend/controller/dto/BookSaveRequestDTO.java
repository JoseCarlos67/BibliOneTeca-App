package com.github.josecarlos67.backend.controller.dto;

import com.github.josecarlos67.backend.model.collection.Book;
import jakarta.validation.constraints.*;

import java.util.Set;
import java.util.UUID;

public record BookSaveRequestDTO(
        @NotBlank(message = "Required field")
        @Size(max = 255, message = "The title must contain a maximum of 255 characters")
        String title,

        @Size(max = 255, message = "The subtitle must contain a maximum of 255 characters")
        String subtitle,

        @NotBlank(message = "Required field")
        @Size(min = 150, message = "The subtitle must contain a minimum of {min} characters")
        String synopsis,

        @NotBlank(message = "Required field")
        String isbn,

        @Positive(message = "The edition number must be a value greater than zero")
        int editionNumber,

        @Positive(message = "The year of publication must be a value greater than zero")
        Integer yearPublication,

        @NotBlank(message = "Required field")
        @Size(max = 50, message = "Must contain a maximum of {max} characters")
        String language,

        @Positive(message = "The page number must be a value greater than zero")
        int pageNumber,

        @Size(max = 2048, message = "Must contain a maximum of {max}")
        String coverUrl,

        UUID publisherId,

        @NotEmpty(message = "The book's author is required")
        Set<@NotNull UUID> authors
) {
}
