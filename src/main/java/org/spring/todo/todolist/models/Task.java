package org.spring.todo.todolist.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "tasks")
public class Task {
    @Id
    private String id;
    private String name;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime executionTime;
    private Boolean isShare;
}
