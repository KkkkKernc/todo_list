package com.example.todo.list;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Kernc
 */
@EnableAutoConfiguration
@ComponentScan
public class TodoListApplication {

    @Bean
    public Runnable createRunnable() {
        return () -> System.out.println("\n Todo list based on spring boot is running ! \n");
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TodoListApplication.class, args);
        context.getBean(Runnable.class).run();
    }
}
