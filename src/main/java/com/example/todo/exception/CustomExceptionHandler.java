package com.example.todo.exception;

import com.example.todo.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomResponseException.class)
    public ResponseEntity<Object> handleBadRequestException(CustomResponseException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ex.getErrorCode())
                .errorMessage(ex.getReason())
                .build();
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }
}