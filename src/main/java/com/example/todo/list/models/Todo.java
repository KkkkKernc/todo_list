package com.example.todo.list.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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
@JsonIgnoreProperties(value = {"createdAt"}, allowGetters = true)
public class Todo {
    @Id
    private String id;

    @NotBlank
    @Size(max = 100)
    @Indexed(unique = true)
    private String title;

    private Boolean completed = false;
    private Date createdAt = new Date();

    public Todo(String title) {
        this.title = title;
    }

    public void setTitle(String title) {
        if (title != null) {
            this.title = title;
        }
    }

    public void setCompleted(Boolean completed) {
        if (completed != null) {
            this.completed = completed;
        }
    }

}
