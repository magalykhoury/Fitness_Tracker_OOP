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

/**
 * Main class for the Fitness Tracker API application.
 *
 * <p>This class bootstraps the Spring Boot application and performs
 * an initial test of the MongoDB connection at startup.
 *
 * <p>It logs important events such as startup progress and environment profiles.
 */
@SpringBootApplication
public class FitnessApiApplication {

    /** Logger instance for logging application events and MongoDB test results. */
    private static final Logger logger = LoggerFactory.getLogger(FitnessApiApplication.class);

    /**
     * The main entry point of the application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        logger.info("Starting Fitness Tracker Application");
        ApplicationContext ctx = SpringApplication.run(FitnessApiApplication.class, args);
        logger.info("Application started successfully");
    }

    /**
     * A {@link CommandLineRunner} bean that runs at application startup
     * to test the MongoDB connection using a given connection string.
     *
     * <p>It also logs the active Spring profiles and available MongoDB databases.
     *
     * @param environment the Spring environment to retrieve active profiles
     * @return a runnable function that tests MongoDB connection
     */
    @Bean
    public CommandLineRunner testConnection(Environment environment) {
        return args -> {
            logger.info("=====================================================");
            logger.info("TESTING MONGODB CONNECTION");
            logger.info("=====================================================");

            String[] activeProfiles = environment.getActiveProfiles();
            logger.info("Active profiles: {}", String.join(", ", activeProfiles));

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
