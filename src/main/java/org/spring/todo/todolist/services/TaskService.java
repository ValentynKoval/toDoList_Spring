package org.spring.todo.todolist.services;

import lombok.RequiredArgsConstructor;
import org.spring.todo.todolist.dto.TaskRequestDto;
import org.spring.todo.todolist.dto.TaskResponseDto;
import org.spring.todo.todolist.mappers.TaskRequestMapper;
import org.spring.todo.todolist.mappers.TaskResponseMapper;
import org.spring.todo.todolist.models.Task;
import org.spring.todo.todolist.models.User;
import org.spring.todo.todolist.repo.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskRequestMapper taskRequestMapper;
    private final TaskResponseMapper taskResponseMapper;
    private final UserService userService;

    public void save(TaskRequestDto taskDto, String username) {
        Task taskToSave = taskRequestMapper.toEntity(taskDto);
        Task savedTask = taskRepository.save(taskToSave);
        userService.addTask(username, savedTask);
    }

    public TaskResponseDto getTask(String taskId, String username) {
        return taskResponseMapper.toDto(findTaskByIdAndUsername(username, taskId));
    }

    public List<TaskResponseDto> getTasksByUsername(String username) {
        List<Task> tasks = userService.findByUsername(username).orElseThrow().getTasks();
        if (tasks.isEmpty()) {
            return null;
        }
        return tasks.stream().map(taskResponseMapper::toDto).collect(Collectors.toList());
    }

    public ResponseEntity<?> updateTask(String username, TaskResponseDto taskResponseDto) {
        if (findTaskByIdAndUsername(username, taskResponseDto.getId()) == null) {
            return ResponseEntity.badRequest().body("The user does not have such a task");
        }
        Task task = taskResponseMapper.toEntity(taskResponseDto);
        taskRepository.save(task);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private void deleteTask(String username, String taskId) {
        Task task = findTaskByIdAndUsername(username, taskId);
        if (task != null) {
            taskRepository.delete(task);
            userService.deleteTask(task, username);
        }
    }

    private Task findTaskByIdAndUsername(String username, String taskId) {
        List<Task> tasks = userService.findByUsername(username).orElseThrow().getTasks();
        Optional<Task> task = tasks.stream().filter(t -> t.getId() == taskId).findAny();
        if (!task.isPresent()) {
            return null;
        }
        return task.orElseThrow();
    }
}
