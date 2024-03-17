package org.spring.todo.todolist.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;

    private String username;
    private String password;
    private String email;

    @DBRef
    private List<Role> roles;

    @DBRef
    private Token token;

    @DBRef
    private List<Task> tasks;
}
