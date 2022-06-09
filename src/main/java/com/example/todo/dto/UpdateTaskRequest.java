package com.example.todo.dto;

import com.example.todo.Enum.ActionEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateTaskRequest implements Serializable {

    private ActionEnum action;
}
