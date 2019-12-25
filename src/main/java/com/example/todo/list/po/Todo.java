package com.example.todo.list.po;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Kernc
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Document(collection = "todos")
@ToString
public class Todo {

    @Id
    private String id;

    @NotBlank
    @Size(max = 100)
    @Indexed(unique = true)
    private String title;

    private Boolean completed;
    private Date createdAt;

    public Todo setTitle(String title) {
        if (title != null) {
            this.title = title;
        }
        return this;
    }

    public Todo setCompleted(Boolean completed) {
        if (completed != null) {
            this.completed = completed;
        }
        return this;
    }
}
