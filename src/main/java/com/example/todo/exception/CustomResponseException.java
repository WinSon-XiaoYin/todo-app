package com.example.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public class CustomResponseException extends ResponseStatusException {
    private final String errorCode;

    public CustomResponseException(HttpStatus status, String reason, String errorCode) {
        super(status, reason);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
