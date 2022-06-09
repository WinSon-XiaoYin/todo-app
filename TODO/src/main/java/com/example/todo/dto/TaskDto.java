package com.example.todo.dto;

import com.example.todo.Enum.StatusEnum;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class TaskDto implements Serializable {

    private Long id;
    private String summary;
    private String dueDate;
    private StatusEnum status;
}
