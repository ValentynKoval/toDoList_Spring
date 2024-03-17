package org.spring.todo.todolist.controllers;

import lombok.RequiredArgsConstructor;
import org.spring.todo.todolist.dto.RoleDto;
import org.spring.todo.todolist.services.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/createNewRole")
    public ResponseEntity<?> createNewRole(@RequestBody RoleDto roleDto) {
        return ResponseEntity.ok(roleService.createNewRole(roleDto));
    }
}
