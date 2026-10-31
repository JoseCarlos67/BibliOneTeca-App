package com.github.josecarlos67.backend.controller.dto;

public record BookSaveRequestDTO(
  String title,
  String subtitle,
  String synopsis
) {
}
