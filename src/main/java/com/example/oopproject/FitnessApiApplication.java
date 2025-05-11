package com.example.oopproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.example.oopproject.repositories")
public class FitnessApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(FitnessApiApplication.class, args);
    }
}
