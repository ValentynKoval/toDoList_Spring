package org.spring.todo.todolist.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "tokens")
public class Token {
    @Id
    private String id;

    @Indexed(unique = true)
    private String token;
}
