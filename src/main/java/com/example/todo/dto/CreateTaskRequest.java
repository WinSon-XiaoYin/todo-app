package com.example.todo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CreateTaskRequest implements Serializable {

    private String summary;
    private String dueDate;
}
