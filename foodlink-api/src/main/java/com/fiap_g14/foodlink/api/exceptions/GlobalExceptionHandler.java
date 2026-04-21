package com.fiap_g14.foodlink.api.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataAlreadyExistsException.class)
    public ResponseEntity<String> handleException(DataAlreadyExistsException ex) {
        return ResponseEntity.status(409).body(ex.getMessage());
    }
}
