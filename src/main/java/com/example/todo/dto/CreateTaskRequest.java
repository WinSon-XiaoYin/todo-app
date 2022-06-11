package com.example.todo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CreateTaskRequest implements Serializable {

    @ApiModelProperty(value = "Task summary", example = "This is a task", required = true)
    private String summary;

    @ApiModelProperty(value = "Task due date", example = "12/06/2022", required = true)
    private String dueDate;
}
