package org.spring.todo.todolist.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskRequestDto {
    private String name;
    private String description;
    private LocalDateTime executionTime;
}
