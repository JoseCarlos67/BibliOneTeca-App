package com.github.josecarlos67.backend.controller.dto;

public record ErrorFieldDTO(
        String field,
        String error
) {
}
