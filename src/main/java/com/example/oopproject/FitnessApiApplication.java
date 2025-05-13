package com.example.oopproject;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@SpringBootApplication
public class FitnessApiApplication {

    private static final Logger logger = LoggerFactory.getLogger(FitnessApiApplication.class);

    public static void main(String[] args) {
        logger.info("Starting Fitness Tracker Application");
        ApplicationContext ctx = SpringApplication.run(FitnessApiApplication.class, args);
        logger.info("Application started successfully");
    }

    @Bean
    public CommandLineRunner testConnection(Environment environment) {
        return args -> {
            logger.info("=====================================================");
            logger.info("TESTING MONGODB CONNECTION");
            logger.info("=====================================================");

            // Log active profiles
            String[] activeProfiles = environment.getActiveProfiles();
            logger.info("Active profiles: {}", String.join(", ", activeProfiles));

            // Test direct connection to MongoDB
            String connectionString = "mongodb+srv://magaly:mohamad@cluster0.ientrf8.mongodb.net/fitness_tracker?retryWrites=true&w=majority";
            logger.info("Testing direct connection to MongoDB with URI: mongodb+srv://magaly:***@cluster0.ientrf8.mongodb.net/fitness_tracker");

            try (MongoClient client = MongoClients.create(connectionString)) {
                logger.info("MongoDB connection successful!");
                logger.info("Available databases:");
                client.listDatabaseNames().forEach(name -> logger.info(" - {}", name));
            } catch (Exception e) {
                logger.error("MongoDB connection test failed: {}", e.getMessage());
            }

            logger.info("=====================================================");
        };
    }
}