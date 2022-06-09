package com.example.todo.controller;

import com.example.todo.Enum.ActionEnum;
import com.example.todo.Enum.StatusEnum;
import com.example.todo.dto.CreateTaskRequest;
import com.example.todo.dto.TaskDto;
import com.example.todo.dto.UpdateTaskRequest;
import com.example.todo.entity.Task;
import com.example.todo.repository.TaskRepository;
import com.example.todo.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
class TaskControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    private String summary;
    private String dueDate;

    private List<TaskDto> tasks;

    private Task task;

    private UpdateTaskRequest updateTaskRequest;
    private CreateTaskRequest createTaskRequest;

    @BeforeEach
    void setUp() {
        tasks = new ArrayList<>();
        dueDate = "08/06/2022";
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
                .dueDate(LocalDate.now())
                .status(StatusEnum.TODO)
                .build();

        updateTaskRequest = new UpdateTaskRequest();
        updateTaskRequest.setAction(ActionEnum.DONE);

        createTaskRequest = new CreateTaskRequest();
        createTaskRequest.setSummary(summary);
        createTaskRequest.setDueDate(dueDate);
    }

    @Test
    void retrieveAllTasks() throws Exception {
        when(taskService.retrieveAllTasks(dueDate)).thenReturn(tasks);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/api/tasks?dueDate=08/06/2022")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].summary").value(summary));
    }

    @Test
    void inquiryTask() throws Exception {
        when(taskService.inquiryTask(1L)).thenReturn(tasks.get(0));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.summary").value(summary));
    }

    @Test
    void inquiryTask_task_not_found_exception() throws Exception {
        when(taskService.inquiryTask(any())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Task is not found"));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void createTask() throws Exception {
        when(taskService.createTask(any())).thenReturn(task);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/api/tasks")
                        .content(asJsonString(createTaskRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void updateTask() throws Exception {
        when(taskRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(task));
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/v1/api/tasks/1")
                        .content(asJsonString(updateTaskRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateTask_task_not_found_exception() throws Exception {
        when(taskRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(task));
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Task is not found")).when(taskService).updateTask(any(), any());
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/v1/api/tasks/1")
                        .content(asJsonString(updateTaskRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTask_op_not_not_accepted_exception() throws Exception {
        when(taskRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(task));
        doThrow(new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid action for current status")).when(taskService).updateTask(any(), any());
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/v1/api/tasks/1")
                        .content(asJsonString(updateTaskRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable());
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            System.out.println(jsonContent);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}