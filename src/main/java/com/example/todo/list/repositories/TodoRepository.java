package com.example.todo.list.repositories;

import com.example.todo.list.models.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Kernc
 */
@Repository
public interface TodoRepository extends MongoRepository<Todo, String> {

}