package com.example.todo.list.controller;

import com.example.todo.list.po.Todo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.result.DeleteResult;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
@ContextConfiguration(classes = {TodoControllerTest.TestConfig.class})
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getTodoList() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Test
    public void createTodo() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/todo")
                        .content("{ \"title\": \"test create todo\" }")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        Assert.assertEquals(200, mvcResult.getResponse().getStatus());

        ObjectMapper objectMapper = new ObjectMapper();
        Todo todo = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), Todo.class);
        Assert.assertNotNull(todo);
        Assert.assertEquals("test create todo", todo.getTitle());
        Assert.assertEquals(false, todo.getCompleted());
    }

    @Test
    public void getTodoById() throws Exception {
        MvcResult mvcResult1 = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        Assert.assertEquals(200, mvcResult1.getResponse().getStatus());

        ObjectMapper objectMapper = new ObjectMapper();
        Todo todo = objectMapper.readValue(mvcResult1.getResponse().getContentAsByteArray(), Todo.class);
        Assert.assertNotNull(todo);
        Assert.assertEquals("test get by id", todo.getTitle());
        Assert.assertEquals(false, todo.getCompleted());

        MvcResult mvcResult2 = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/todos/2")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        Assert.assertEquals(404, mvcResult2.getResponse().getStatus());
    }

    @Test
    public void updateTodo() throws Exception {
        MvcResult mvcResult1 = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/todos/3")
                        .content("{ \"title\": \"test change todo\", \"completed\": true }")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        Assert.assertEquals(200, mvcResult1.getResponse().getStatus());
        ObjectMapper objectMapper = new ObjectMapper();
        Todo todo = objectMapper.readValue(mvcResult1.getResponse().getContentAsByteArray(), Todo.class);
        Assert.assertNotNull(todo);
        Assert.assertEquals("test change todo", todo.getTitle());
        Assert.assertEquals(true, todo.getCompleted());

        MvcResult mvcResult2 = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/todos/4")
                        .content("{ \"title\": \"test change todo\", \"completed\": true }")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        Assert.assertEquals(404, mvcResult2.getResponse().getStatus());
    }

    @Test
    public void deleteTodo() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Configuration
    static class TestConfig {
        @Bean
        TodoController TodoController() {
            return new TodoController();
        }

        @Bean
        MongoOperations mockMongoOperations() {
            MongoOperations mongoOperations = Mockito.mock(MongoOperations.class);

            Mockito.when(mongoOperations.findAll(Todo.class))
                    .thenReturn(Arrays.asList(new Todo[0]));

            Mockito.when(mongoOperations.save(any()))
                    .thenAnswer(invocation -> {
                        Object arg = invocation.getArguments()[0];
                        Field id = arg.getClass().getDeclaredField("id");
                        if (id != null) {
                            id.setAccessible(true);
                            id.set(arg, UUID.randomUUID().toString());
                        }
                        return arg;
                    });

            Mockito.when(mongoOperations.findOne(Query.query(Criteria.where("id").is("1")), Todo.class))
                    .thenReturn(new Todo()
                            .setId("1")
                            .setTitle("test get by id")
                            .setCompleted(false)
                    );

            Mockito.when(mongoOperations.findOne(Query.query(Criteria.where("id").is("3")), Todo.class))
                    .thenReturn(new Todo()
                            .setId("3")
                            .setTitle("test get by id")
                            .setCompleted(false)
                    );

            DeleteResult deleteResult = Mockito.mock(DeleteResult.class, Mockito.CALLS_REAL_METHODS);
            Mockito.when(mongoOperations.remove(Query.query(Criteria.where("id").is("1")), Todo.class))
                    .thenReturn(deleteResult);

            return mongoOperations;
        }

    }
}