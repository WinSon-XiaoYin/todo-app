package com.example.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;


@Data
@Builder
@AllArgsConstructor
public class ErrorResponse implements Serializable {

    private final String errorMessage;
    private final String errorCode;

}
