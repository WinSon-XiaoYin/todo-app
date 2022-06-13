package com.example.todo.repository;

import com.example.todo.entity.Task;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface Task1Repository extends PagingAndSortingRepository<Task, Long>, JpaSpecificationExecutor<Task> {
}
