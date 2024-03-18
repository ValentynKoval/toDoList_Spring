package org.spring.todo.todolist.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.spring.todo.todolist.dto.TaskRequestDto;
import org.spring.todo.todolist.dto.TaskResponseDto;
import org.spring.todo.todolist.models.Task;
import org.spring.todo.todolist.services.TaskService;
import org.spring.todo.todolist.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;
    @GetMapping
    public ResponseEntity<?> getAllTasks(Principal connectedUser) {
        List<TaskResponseDto> tasks = taskService.getTasksByUsername(connectedUser.getName());
        if (tasks == null) {
            return ResponseEntity.badRequest().body("The user has no tasks");
        }
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<?> getTask(@PathVariable String taskId, Principal connectedUser) {
        TaskResponseDto taskResponseDto = taskService.getTask(taskId, connectedUser.getName());
        if (taskResponseDto == null) {
            return ResponseEntity.badRequest().body("There is no such task for the user");
        }
        return ResponseEntity.ok(taskResponseDto);
    }

    @PostMapping("")
    public ResponseEntity<?> saveNewTask(@RequestBody TaskRequestDto taskDto, Principal connectedUser) {
        taskService.save(taskDto, connectedUser.getName());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<?> updateTask(@RequestBody TaskRequestDto taskRequestDto, @PathVariable String taskId, Principal connectedUser) {
        return ResponseEntity.ok().body(taskService.updateTask(connectedUser.getName(), taskRequestDto, taskId));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable String taskId, Principal connectedUser) {

        taskService.deleteTask(connectedUser.getName(), taskId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/share/{taskId}")
    public ResponseEntity<?> getCurrentRequestInfo(HttpServletRequest request, @PathVariable String taskId, Principal connectedUser) {
        if (taskService.findTaskByIdAndUsername(connectedUser.getName(), taskId) == null) {
            return ResponseEntity.badRequest().body("You do not have access to create a link for this task");
        }
        taskService.changeIsShare(taskId);
        return ResponseEntity.ok().body(getAppUrl(request, taskId));
    }

    @PostMapping("/share_path/{taskId}")
    public ResponseEntity<?> addSharedTask(@PathVariable String taskId, Principal connectedUser) {
        if (taskService.addTaskById(connectedUser.getName(), taskId)) {
            return ResponseEntity.ok(HttpStatus.OK);
        }
        return ResponseEntity.badRequest().body("The distribution function of this task is closed.");
    }

    private String getAppUrl(HttpServletRequest request, String taskId) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/task/share_path/" + taskId;
    }
}
