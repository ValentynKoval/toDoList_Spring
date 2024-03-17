package org.spring.todo.todolist.mappers;

import org.spring.todo.todolist.dto.TaskDto;
import org.spring.todo.todolist.models.Task;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TaskMapper implements EntityMapper<Task, TaskDto> {
    @Override
    public Task toEntity(TaskDto dto) {
        Task task = new Task();
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setCreateTime(LocalDateTime.now());
        task.setExecutionTime(dto.getExecutionTime());
        task.setIsShare(false);
        return task;
    }

    @Override
    public TaskDto toDto(Task toEntity) {
        TaskDto taskDto = new TaskDto();
        taskDto.setName(toEntity.getName());
        taskDto.setDescription(toEntity.getDescription());
        taskDto.setExecutionTime(toEntity.getExecutionTime());
        return taskDto;
    }
}
