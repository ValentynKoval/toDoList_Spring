package org.spring.todo.todolist.mappers;

import org.spring.todo.todolist.dto.TaskResponseDto;
import org.spring.todo.todolist.models.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskResponseMapper implements EntityMapper<Task, TaskResponseDto> {
    @Override
    public Task toEntity(TaskResponseDto dto) {
        Task task = new Task();
        task.setId(dto.getId());
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setCreateTime(dto.getCreateTime());
        task.setExecutionTime(dto.getExecutionTime());
        task.setIsShare(false);
        return task;
    }

    @Override
    public TaskResponseDto toDto(Task toEntity) {
        TaskResponseDto taskResponseDto = new TaskResponseDto();
        taskResponseDto.setId(toEntity.getId());
        taskResponseDto.setName(toEntity.getName());
        taskResponseDto.setDescription(toEntity.getDescription());
        taskResponseDto.setCreateTime(toEntity.getCreateTime());
        taskResponseDto.setExecutionTime(toEntity.getExecutionTime());
        return taskResponseDto;
    }
}
