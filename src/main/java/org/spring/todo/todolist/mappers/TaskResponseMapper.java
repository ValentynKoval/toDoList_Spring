package org.spring.todo.todolist.mappers;

import org.spring.todo.todolist.dto.TaskResponseDto;
import org.spring.todo.todolist.models.Task;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TaskResponseMapper implements EntityMapper<Task, TaskResponseDto> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Task toEntity(TaskResponseDto dto) {
        Task task = new Task();
        task.setId(dto.getId());
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setCreateTime(LocalDateTime.parse(dto.getCreateTime(), formatter));
        task.setExecutionTime(LocalDateTime.parse(dto.getExecutionTime(), formatter));
        task.setIsShare(false);
        return task;
    }

    @Override
    public TaskResponseDto toDto(Task toEntity) {
        TaskResponseDto taskResponseDto = new TaskResponseDto();
        taskResponseDto.setId(toEntity.getId());
        taskResponseDto.setName(toEntity.getName());
        taskResponseDto.setDescription(toEntity.getDescription());
        taskResponseDto.setCreateTime(toEntity.getCreateTime().format(formatter));
        taskResponseDto.setExecutionTime(toEntity.getExecutionTime().format(formatter));
        return taskResponseDto;
    }
}
