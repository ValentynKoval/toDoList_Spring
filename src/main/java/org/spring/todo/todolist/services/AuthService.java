package org.spring.todo.todolist.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.spring.todo.todolist.dto.AuthDto;
import org.spring.todo.todolist.dto.RegistrationUserDto;
import org.spring.todo.todolist.exceptions.AppError;
import org.spring.todo.todolist.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> createAuthTokens(HttpServletResponse response, AuthDto authDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Неправильный логин или пароль"), HttpStatus.UNAUTHORIZED);
        }
        return loginUser(response, authDto.getUsername());
    }

    public ResponseEntity<?> loginUser(HttpServletResponse response, String username) {
        UserDetails userDetails = userService.loadUserByUsername(username);
        String token = jwtService.generateToken(userDetails);
        setTokenToCookie(response, token);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    public void setTokenToCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("token", token);
        response.addCookie(cookie);
    }

    public ResponseEntity<?> createNewUser(HttpServletResponse response, RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пароли не совпадают"), HttpStatus.BAD_REQUEST);
        }
        if (userService.findByUsername(registrationUserDto.getUsername()).isPresent()) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пользователь с таким именем уже существует"), HttpStatus.BAD_REQUEST);
        }
        User user = userService.createNewUser(registrationUserDto);
        return loginUser(response, user.getUsername());
    }

    public ResponseEntity<?> refreshTokens(HttpServletResponse response, String username) {
        String refreshToken = userService.findTokenByUsername(username);
        List<String> roles = jwtService.getRoles(refreshToken);
        String newEccessToken = jwtService.refreshTokensByClaims(roles, username);
        setTokenToCookie(response, newEccessToken);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
