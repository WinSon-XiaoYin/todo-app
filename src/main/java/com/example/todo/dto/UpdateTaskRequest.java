package com.example.todo.dto;

import com.example.todo.Enum.ActionEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateTaskRequest implements Serializable {

    @ApiModelProperty(required = true)
    private ActionEnum action;
}
