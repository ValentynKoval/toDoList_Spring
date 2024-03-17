package org.spring.todo.todolist.services;

import lombok.RequiredArgsConstructor;
import org.spring.todo.todolist.dto.RegistrationUserDto;
import org.spring.todo.todolist.models.Task;
import org.spring.todo.todolist.models.Token;
import org.spring.todo.todolist.models.User;
import org.spring.todo.todolist.repo.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow();
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()));
    }

    public void setToken(String username, Token token) {
        User user = findByUsername(username).orElseThrow();
        if (user.getToken() != null) {
            tokenService.deleteByToken(user.getToken().getToken());
        }
        user.setToken(token);
        userRepository.save(user);
    }

    public User createNewUser(RegistrationUserDto registrationUserDto) {
        User user = new User();
        user.setUsername(registrationUserDto.getUsername());
        user.setEmail(registrationUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        user.setRoles(List.of(roleService.getUserRole()));
        return userRepository.save(user);
    }

    public String findTokenByUsername(String username) {
        User user = findByUsername(username).orElseThrow();
        return user.getToken().getToken();
    }

    public void deleteUserToken(String username) {
        User user = findByUsername(username).orElseThrow();
        user.setToken(null);
        userRepository.save(user);
    }

    public void addTask(String username, Task task) {
        User user = findByUsername(username).orElseThrow();
        user.getTasks().add(task);
        userRepository.save(user);
    }

    public void deleteTask(Task task, String username) {
        User user = findByUsername(username).orElseThrow();
        user.getTasks().remove(task);
    }
}
