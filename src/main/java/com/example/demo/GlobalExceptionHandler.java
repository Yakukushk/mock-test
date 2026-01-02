package com.example.demo;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handle(UserNotFoundException ex) {

    ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage()
    );

    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(errorResponse);
  }
}
