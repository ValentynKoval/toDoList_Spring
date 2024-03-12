package org.spring.todo.todolist.services;

import lombok.RequiredArgsConstructor;
import org.spring.todo.todolist.models.Token;
import org.spring.todo.todolist.models.User;
import org.spring.todo.todolist.repo.RoleRepository;
import org.spring.todo.todolist.repo.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("Пользователь %s не найден", username)));
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()));
    }

    public void createNewUser (User user) { // также нужно сохранить токен(либо же создать метод авторизации пользователя и уже в него добавлять токен)
        user.setRoles(List.of(roleRepository.findByName("ROLE_USER").get()));
        userRepository.save(user);
    }

    public void setToken(String username, Token token) {
        User user = findByUsername(username);
        user.setToken(token);
        userRepository.save(user);
    }
}