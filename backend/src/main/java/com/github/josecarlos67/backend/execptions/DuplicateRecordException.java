package com.github.josecarlos67.backend.execptions;

public class DuplicateRecordException extends RuntimeException {
  public DuplicateRecordException(String message) {
    super(message);
  }
}
