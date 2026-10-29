package com.github.josecarlos67.backend.execptions;

public class OperationNotPermittedException extends RuntimeException {
  public OperationNotPermittedException(String message) {
    super(message);
  }
}
