package com.github.josecarlos67.backend.controller.common;

import com.github.josecarlos67.backend.controller.dto.ErrorFieldDTO;
import com.github.josecarlos67.backend.controller.dto.ResponseErrorDTO;
import com.github.josecarlos67.backend.execptions.DuplicateRecordException;
import com.github.josecarlos67.backend.execptions.OperationNotPermittedException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ResponseErrorDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    List<FieldError> fieldErrors = e.getFieldErrors();

    List<ErrorFieldDTO> errorFieldDTOS = fieldErrors.stream().map(fe -> new ErrorFieldDTO(fe.getField(), fe.getDefaultMessage())).collect(Collectors.toList());

    return new ResponseErrorDTO(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Validation error!", errorFieldDTOS);
  }

  @ExceptionHandler(DuplicateRecordException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ResponseErrorDTO handleDuplicateRecordException(DuplicateRecordException e) {
    return ResponseErrorDTO.conflict(e.getMessage());
  }

  @ExceptionHandler(OperationNotPermittedException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseErrorDTO handleOperationNotPermittedException(OperationNotPermittedException e) {
    return ResponseErrorDTO.standardError(e.getMessage());
  }

  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseErrorDTO handleUnhandledErrorsException(RuntimeException e) {
    return new ResponseErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred!", List.of());
  }

}
