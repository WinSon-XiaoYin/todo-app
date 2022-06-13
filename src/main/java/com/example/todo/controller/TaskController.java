package com.example.todo.controller;

import com.example.todo.dto.*;
import com.example.todo.entity.Task;
import com.example.todo.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@Api(tags = "Task Management")
@RestController
@RequestMapping("/v1/api")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @ApiOperation("List Task")
    @GetMapping("/tasks")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "return all the tasks", response = TaskDto.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Please input a valid due date", response = ErrorResponse.class)
    })
    public ResponseEntity<List<TaskDto>> retrieveAllTasks(@RequestParam(required = false) String dueDate) {
        return ResponseEntity.ok(taskService.retrieveAllTasks(dueDate));
    }

    @ApiOperation("Inquiry Task")
    @GetMapping("/tasks/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "return task detail", response = TaskDto.class),
            @ApiResponse(code = 404, message = "Task is not found", response = ErrorResponse.class)
    })
    public ResponseEntity<TaskDto> inquiryTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.inquiryTask(id));
    }

    @ApiOperation("Create Task")
    @PostMapping("/tasks")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "task is created"),
            @ApiResponse(code = 400, message = "Please input a valid due date", response = ErrorResponse.class),
            @ApiResponse(code = 406, message = "Due date must be later than or equals to today", response = ErrorResponse.class)
    })
    public ResponseEntity<String> createTask(@RequestBody CreateTaskRequest request) {
        Task task = taskService.createTask(request);
        String newUri = String.format("/v1/api/tasks/%s", task.getId());
        return ResponseEntity.created(URI.create(newUri)).build();
    }

    @ApiOperation("Update Task")
    @PatchMapping("/tasks/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "task is updated"),
            @ApiResponse(code = 404, message = "Task is not found", response = ErrorResponse.class),
            @ApiResponse(code = 406, message = "Invalid action for current status", response = ErrorResponse.class)
    })
    public ResponseEntity<String> updateTask(@PathVariable Long id, @RequestBody UpdateTaskRequest request) {
        taskService.updateTask(id, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tasks/list")
    public Page<Task> taskList(PageParamRequest request) {
        return taskService.taskList(request);
    }
}
