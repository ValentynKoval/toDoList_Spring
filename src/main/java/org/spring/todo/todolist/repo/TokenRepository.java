package org.spring.todo.todolist.repo;

import org.spring.todo.todolist.models.Token;
import org.spring.todo.todolist.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends MongoRepository<Token, String> {
}
