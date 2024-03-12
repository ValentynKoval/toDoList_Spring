package org.spring.todo.todolist.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.spring.todo.todolist.dto.AuthDto;
import org.spring.todo.todolist.dto.RegistrationUserDto;
import org.spring.todo.todolist.exceptions.AppError;
import org.spring.todo.todolist.models.User;
import org.spring.todo.todolist.services.JwtService;
import org.spring.todo.todolist.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> userAuthorization(HttpServletResponse response, @RequestBody AuthDto authDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Неправильный логин или пароль"), HttpStatus.UNAUTHORIZED);
        }
        return loginUser(response, authDto);
    }

    public ResponseEntity loginUser(HttpServletResponse response, AuthDto authDto) {
        UserDetails userDetails = userService.loadUserByUsername(authDto.getUsername());
        String token = jwtService.generateToken(userDetails);
        Cookie cookie = new Cookie("token", token);
        response.addCookie(cookie);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(HttpServletResponse response, @RequestBody RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пароли не совпадают"), HttpStatus.BAD_REQUEST);
        }
        if (userService.findByUsername(registrationUserDto.getUsername()) != null) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пользователь с таким именем уже существует"), HttpStatus.BAD_REQUEST);
        }
        User user = userService.createNewUser(registrationUserDto);
        return loginUser(response, new AuthDto(user.getUsername(), user.getPassword()));
    }
}
