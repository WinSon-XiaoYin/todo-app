package com.example.todo.dto;

import com.example.todo.Enum.StatusEnum;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
public class PageParamRequest {

    private String id;
    private Integer pageSize = 10;
    private Integer pageNumber = 1;
    private String summary;
    private StatusEnum status;
    private List<StatusEnum> statusList;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;


}
