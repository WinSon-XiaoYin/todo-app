package com.example.todo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleBadRequestException(ResponseStatusException ex) {
        // if you want you can do some extra processing with message and status of an exception
        // or you can return it without any processing like this:
        Map<String, String> body = new HashMap<>();
        body.put("errorMessage", ex.getReason());
        return new ResponseEntity<>(body, ex.getStatus());
    }
}