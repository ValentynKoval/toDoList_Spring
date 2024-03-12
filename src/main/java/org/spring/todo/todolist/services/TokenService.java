package org.spring.todo.todolist.services;

import lombok.RequiredArgsConstructor;
import org.spring.todo.todolist.models.Token;
import org.spring.todo.todolist.models.User;
import org.spring.todo.todolist.repo.TokenRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    public Token saveNewToken(String token) {
        Token newToken = new Token();
        newToken.setToken(token);
        return tokenRepository.save(newToken);
    }
}
