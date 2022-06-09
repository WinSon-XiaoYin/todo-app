package com.example.todo.service;

import com.example.todo.Enum.ActionEnum;
import com.example.todo.Enum.StatusEnum;
import com.example.todo.dto.CreateTaskRequest;
import com.example.todo.dto.TaskDto;
import com.example.todo.dto.UpdateTaskRequest;
import com.example.todo.entity.Task;
import com.example.todo.repository.TaskRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    private String summary;
    private String dueDate;

    private List<TaskDto> tasks;

    private Task task;

    private UpdateTaskRequest updateTaskRequest;
    private CreateTaskRequest createTaskRequest;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Before
    public void setUp() {
        tasks = new ArrayList<>();
        dueDate = LocalDate.now().format(formatter);
        summary = "This is a test";
        TaskDto t = TaskDto.builder()
                .id(1L)
                .summary(summary)
                .dueDate(dueDate)
                .status(StatusEnum.TODO)
                .build();
        tasks.add(t);

        task = Task.builder()
                .id(1L)
                .summary(summary)
                .dueDate(LocalDate.parse(dueDate, formatter))
                .status(StatusEnum.TODO)
                .build();

        updateTaskRequest = new UpdateTaskRequest();
        updateTaskRequest.setAction(ActionEnum.DONE);

        createTaskRequest = new CreateTaskRequest();
        createTaskRequest.setSummary(summary);
        createTaskRequest.setDueDate(dueDate);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void retrieveAllTasks() {
        when(taskRepository.findByDueDate(any())).thenReturn(Collections.singletonList(task));
        List<TaskDto> taskDtos = taskService.retrieveAllTasks(dueDate);
        Assertions.assertEquals(tasks, taskDtos);
    }

    @Test
    public void retrieveAllTasks_getAll() {
        when(taskRepository.findAll()).thenReturn(Collections.singletonList(task));
        List<TaskDto> taskDtos = taskService.retrieveAllTasks(null);
        Assertions.assertEquals(tasks, taskDtos);
    }

    @Test(expected=RuntimeException.class)
    public void retrieveAllTasks_invalid_dueDate() {
        List<TaskDto> taskDtos = taskService.retrieveAllTasks("11111");

    }

    @Test
    public void inquiryTask() {
        when(taskRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(task));
        TaskDto task1 = taskService.inquiryTask(1L);
        Assertions.assertEquals(task1, tasks.get(0));
    }

    @Test(expected = ResponseStatusException.class)
    public void inquiryTask_Task_Not_Found() {
        when(taskRepository.findById(any())).thenReturn(Optional.empty());
        TaskDto task1 = taskService.inquiryTask(1L);
    }

    @Test
    public void createTask() {
        when(taskRepository.save(any())).thenReturn(task);
        Task t = taskService.createTask(createTaskRequest);
        Assertions.assertEquals(t, task);
    }

    @Test(expected = ResponseStatusException.class)
    public void createTask_due_date_before_today() {
        createTaskRequest.setDueDate("09/06/2021");
        Task t = taskService.createTask(createTaskRequest);
    }

    @Test(expected=RuntimeException.class)
    public void createTask_invalid_dueDate() {
        createTaskRequest.setDueDate("11111");
        Task t = taskService.createTask(createTaskRequest);

    }
    @Test
    public void updateTask() {
        when(taskRepository.findById(any())).thenReturn(Optional.ofNullable(task));
        taskService.updateTask(1L, updateTaskRequest);
        verify(taskRepository, times(1)).save(task);
    }

    @Test(expected = ResponseStatusException.class)
    public void updateTask_Action_Not_Acceptable() {
        when(taskRepository.findById(any())).thenReturn(Optional.ofNullable(task));
        updateTaskRequest.setAction(ActionEnum.ROLLBACK);
        taskService.updateTask(1L, updateTaskRequest);
    }

    @Test(expected = ResponseStatusException.class)
    public void updateTask_Task_Not_Found() {
        when(taskRepository.findById(any())).thenReturn(Optional.empty());
        taskService.updateTask(1L, updateTaskRequest);
    }
}