package com.example.todo.list.controller;

import com.example.todo.list.po.Todo;
import com.mongodb.client.result.DeleteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Kernc
 */
@RestController
@RequestMapping("/api")
public class TodoController {

    @Autowired
    MongoOperations mongoOperations;

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoController.class);

    @GetMapping("/todos")
    public List<Todo> getTodoList() {
        return mongoOperations.findAll(Todo.class);
    }

    @PostMapping("/todo")
    public Todo createTodo(@Valid @RequestBody Todo todo) {
        todo.setCompleted(false);
        todo.setCreatedAt(new Date());
        return mongoOperations.save(todo);
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable("id") String id) {
        return Optional.ofNullable(mongoOperations.findOne(Query.query(Criteria.where("id").is(id)), Todo.class))
                .map(ResponseEntity.ok()::body)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/todos/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable("id") String id,
                                           @RequestBody Todo todo) {
        return Optional.ofNullable(mongoOperations.findOne(Query.query(Criteria.where("id").is(id)), Todo.class))
                .map((Todo todoData) -> {
                    todoData.setTitle(todo.getTitle());
                    todoData.setCompleted(todo.getCompleted());
                    mongoOperations.save(todoData);
                    return ResponseEntity.ok().body(todoData);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<DeleteResult> deleteTodo(@PathVariable("id") String id) {
        DeleteResult result = mongoOperations.remove(Query.query(Criteria.where("id").is(id)), Todo.class);
        return Optional.ofNullable(result)
                .map(ResponseEntity.ok()::body)
                .orElse(ResponseEntity.notFound().build());
    }
}
