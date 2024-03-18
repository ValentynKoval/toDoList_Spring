package org.spring.todo.todolist.mappers;

import org.spring.todo.todolist.dto.TaskRequestDto;
import org.spring.todo.todolist.models.Task;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TaskRequestMapper implements EntityMapper<Task, TaskRequestDto> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Task toEntity(TaskRequestDto dto) {
        Task task = new Task();
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setCreateTime(LocalDateTime.now());
        task.setExecutionTime(LocalDateTime.parse(dto.getExecutionTime(), formatter));
        task.setIsShare(false);
        return task;
    }

    @Override
    public TaskRequestDto toDto(Task toEntity) {
        TaskRequestDto taskDto = new TaskRequestDto();
        taskDto.setName(toEntity.getName());
        taskDto.setDescription(toEntity.getDescription());
        taskDto.setExecutionTime(toEntity.getExecutionTime().format(formatter));
        return taskDto;
    }
}