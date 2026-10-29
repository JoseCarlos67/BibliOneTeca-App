package com.github.josecarlos67.backend.controller.dto;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.List;

public record ErrorResponseDTO(
        int status,
        String message,
        List<FieldError> errors
) {

  public static ErrorResponseDTO standardError(String message) {
    return new ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), message, List.of());
  }

  public static ErrorResponseDTO conflict(String message) {
    return new ErrorResponseDTO(HttpStatus.CONFLICT.value(), message, List.of());
  }

}
