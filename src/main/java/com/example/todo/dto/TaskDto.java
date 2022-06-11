package com.example.todo.dto;

import com.example.todo.Enum.StatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class TaskDto implements Serializable {

    @ApiModelProperty(example = "1")
    private Long id;

    @ApiModelProperty(example = "This is a task")
    private String summary;

    @ApiModelProperty(example = "12/06/2022")
    private String dueDate;

    @ApiModelProperty(example = "TODO")
    private StatusEnum status;
}
