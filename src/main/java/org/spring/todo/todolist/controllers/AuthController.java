package org.spring.todo.todolist.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.spring.todo.todolist.dto.AuthDto;
import org.spring.todo.todolist.dto.RegistrationUserDto;
import org.spring.todo.todolist.dto.UsernameDto;
import org.spring.todo.todolist.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> userAuthorization(HttpServletResponse response, @RequestBody AuthDto authDto) {

        return authService.createAuthTokens(response, authDto);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(HttpServletResponse response, @RequestBody RegistrationUserDto registrationUserDto) {
        return authService.createNewUser(response, registrationUserDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshTokens(HttpServletResponse response, @RequestBody UsernameDto usernameDto) {
        return authService.refreshTokens(response, usernameDto.getUsername());
    }
}
