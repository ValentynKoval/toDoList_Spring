package org.spring.todo.todolist.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskResponseDto {
    private String id;
    private String name;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime executionTime;
}
