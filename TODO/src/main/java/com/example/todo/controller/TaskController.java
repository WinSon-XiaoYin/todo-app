package com.example.todo.controller;

import com.example.todo.dto.CreateTaskRequest;
import com.example.todo.dto.TaskDto;
import com.example.todo.dto.UpdateTaskRequest;
import com.example.todo.entity.Task;
import com.example.todo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/v1/api")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDto>> retrieveAllTasks(@Nullable @RequestParam String dueDate) {
        return ResponseEntity.ok(taskService.retrieveAllTasks(dueDate));
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskDto> inquiryTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.inquiryTask(id));
    }

    @PostMapping("/tasks")
    public ResponseEntity<String> createTask(@RequestBody CreateTaskRequest request) {
        Task task = taskService.createTask(request);
        String newUri = String.format("/v1/api/tasks/%s", task.getId());
        return ResponseEntity.created(URI.create(newUri)).build();
    }


    @PatchMapping("/tasks/{id}")
    public ResponseEntity<String> updateTask(@PathVariable Long id, @RequestBody UpdateTaskRequest request) {
        taskService.updateTask(id, request);
        return ResponseEntity.ok().build();
    }
}
