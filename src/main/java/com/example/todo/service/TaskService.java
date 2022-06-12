package com.example.todo.service;

import com.example.todo.Enum.ErrorCode;
import com.example.todo.Enum.StatusEnum;
import com.example.todo.dto.CreateTaskRequest;
import com.example.todo.dto.TaskDto;
import com.example.todo.dto.UpdateTaskRequest;
import com.example.todo.entity.Task;
import com.example.todo.exception.CustomResponseException;
import com.example.todo.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Slf4j
@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public List<TaskDto> retrieveAllTasks(String dueDate) {
        List<TaskDto> taskList = new ArrayList<>();
        List<Task> tasks;
        if (dueDate != null) {
            LocalDate date;
            try {
                date = LocalDate.parse(dueDate.trim(), formatter);
            } catch (DateTimeParseException e) {
                log.error("Due date {} is invalid", dueDate);
                throw new CustomResponseException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DUE_DATE_FORMAT.getMessage(),
                        ErrorCode.INVALID_DUE_DATE_FORMAT.getCode());
            }
            log.info("Retrieve all tasks that will expire on {}", dueDate);
            tasks = taskRepository.findByDueDate(date);
        } else {
            log.info("Retrieve all tasks");
            tasks = taskRepository.findAll();
        }
        for (Task task: tasks) {
            TaskDto t = TaskDto.builder()
                    .id(task.getId())
                    .summary(task.getSummary())
                    .dueDate(task.getDueDate().format(formatter))
                    .status(task.getStatus())
                    .build();
            taskList.add(t);
        }
        return taskList;
    }

    public TaskDto inquiryTask(Long id) {
        log.info("Inquiry task {}", id);
        Optional<Task> task = taskRepository.findById(id);
        task.orElseThrow(() -> {
            log.error("Task {} is not found", id);
            return new CustomResponseException(HttpStatus.NOT_FOUND, ErrorCode.TASK_NOT_FOUND.getMessage(),
                    ErrorCode.TASK_NOT_FOUND.getCode());
        });
        Task t = task.get();
        return TaskDto.builder()
                .id(t.getId())
                .summary(t.getSummary())
                .dueDate(t.getDueDate().format(formatter))
                .status(t.getStatus())
                .build();
    }

    public Task createTask(CreateTaskRequest request) {
        LocalDate dueDate;
        try {
            dueDate = LocalDate.parse(request.getDueDate(), formatter);
        } catch (DateTimeParseException e) {
            log.error("Due date {} is invalid", request.getDueDate());
            throw new CustomResponseException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DUE_DATE_FORMAT.getMessage(),
                    ErrorCode.INVALID_DUE_DATE_FORMAT.getCode());
        }

        if (dueDate.isBefore(LocalDate.now())) {
            log.error("Due date {} must be later than or equals to today", dueDate);
            throw new CustomResponseException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DUE_DATE.getMessage(),
                    ErrorCode.INVALID_DUE_DATE.getCode());
        }

        log.info("Create task {}, and the task will expire on {}", request.getSummary(), request.getDueDate());
        Task task = Task.builder()
                .summary(request.getSummary())
                .dueDate(dueDate)
                .status(StatusEnum.TODO)
                .build();
        return taskRepository.save(task);
    }

    public void updateTask(Long id, UpdateTaskRequest request) {
        log.info("Update task {} with action {}", id, request.getAction());
        Optional<Task> t = taskRepository.findById(id);
        t.orElseThrow(() -> {
            log.error("Task {} is not found", id);
            return new CustomResponseException(HttpStatus.NOT_FOUND, ErrorCode.TASK_NOT_FOUND.getMessage(),
                    ErrorCode.TASK_NOT_FOUND.getCode());
        });

        Task task = t.get();
        switch (request.getAction()) {
            case ROLLBACK:
                if (task.getStatus().equals(StatusEnum.DONE)) {
                    log.info("Update task {} to status {}", task.getId(), StatusEnum.INPROGRESS.getStatus());
                    task.setStatus(StatusEnum.INPROGRESS);
                } else if (task.getStatus().equals(StatusEnum.INPROGRESS)) {
                    log.info("Update task {} to status {}", task.getId(), StatusEnum.TODO.getStatus());
                    task.setStatus(StatusEnum.TODO);
                } else {
                    log.error("Invalid action {} on task {}, current status is {}", request.getAction(), id, task.getStatus());
                    // Throw invalid action
                    throw new CustomResponseException(HttpStatus.NOT_FOUND, ErrorCode.UNACCEPTABLE_ACTION.getMessage(),
                            ErrorCode.UNACCEPTABLE_ACTION.getCode());
                }
                break;
            case DONE:
                if (task.getStatus().equals(StatusEnum.TODO) || task.getStatus().equals(StatusEnum.INPROGRESS)) {
                    log.info("Update task {} to status {}", task.getId(), StatusEnum.DONE.getStatus());
                    task.setStatus(StatusEnum.DONE);
                } else {
                    log.error("Invalid action {} on task {}, current status is {}", request.getAction(), id, task.getStatus());
                    // Throw invalid action
                    throw new CustomResponseException(HttpStatus.NOT_FOUND, ErrorCode.UNACCEPTABLE_ACTION.getMessage(),
                            ErrorCode.UNACCEPTABLE_ACTION.getCode());
                }
                break;
            case START:
                if (task.getStatus().equals(StatusEnum.TODO)) {
                    log.info("Update task {} to status {}", task.getId(), StatusEnum.INPROGRESS.getStatus());
                    task.setStatus(StatusEnum.INPROGRESS);
                } else {
                    log.error("Invalid action {} on task {}, current status is {}", request.getAction(), id, task.getStatus());
                    // Throw invalid action
                    throw new CustomResponseException(HttpStatus.NOT_FOUND, ErrorCode.UNACCEPTABLE_ACTION.getMessage(),
                            ErrorCode.UNACCEPTABLE_ACTION.getCode());
                }
                break;
        }
        taskRepository.save(task);
    }

}
