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
        Task task = findTaskByIdAndUsername(username, taskId);
        if (task == null) {
            return null;
        }
        return taskResponseMapper.toDto(task);
    }

    public List<TaskResponseDto> getTasksByUsername(String username) {
        List<Task> tasks = userService.findByUsername(username).orElseThrow().getTasks();
        if (tasks.isEmpty()) {
            return null;
        }
        return tasks.stream().map(taskResponseMapper::toDto).collect(Collectors.toList());
    }

    public String updateTask(String username, TaskRequestDto taskRequestDto, String taskId) {
        if (findTaskByIdAndUsername(username, taskId) == null) {
            return "The user does not have such a task";
        }
        Task task = taskRequestMapper.toEntity(taskRequestDto);
        task.setId(taskId);
        taskRepository.save(task);
        return "success";
    }

    public void deleteTask(String username, String taskId) {
        Task task = findTaskByIdAndUsername(username, taskId);
        if (task != null) {
            taskRepository.delete(task);
            userService.deleteTask(task, username);
        }
    }

    public Task findTaskByIdAndUsername(String username, String taskId) {
        List<Task> tasks = userService.findByUsername(username).orElseThrow().getTasks();
        Optional<Task> task = tasks.stream().filter(t -> t.getId().equals(taskId)).findAny();
        if (!task.isPresent()) {
            return null;
        }
        return task.orElseThrow();
    }

    public boolean addTaskById(String username, String taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        if (!task.getIsShare()) {
            return false;
        }
        userService.addTask(username, task);
        return true;
    }

    public void changeIsShare(String taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        task.setIsShare(true);
        taskRepository.save(task);
    }
}
