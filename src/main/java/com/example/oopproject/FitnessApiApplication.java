package com.example.oopproject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class FitnessApiApplication {

    private static final Logger logger = LoggerFactory.getLogger(FitnessApiApplication.class);

    @Autowired
    private Environment environment;

    @Autowired
    private MongoTemplate mongoTemplate;

    public static void main(String[] args) {
        SpringApplication.run(FitnessApiApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void testMongoDbConnection() {
        logger.info("Testing MongoDB connection...");

        try {
            // Get and log active profiles
            String[] activeProfiles = environment.getActiveProfiles();
            logger.info("Active profiles: {}", Arrays.toString(activeProfiles));

            // Get MongoDB URI from environment (safely masking password)
            String mongoUriRaw = environment.getProperty("spring.data.mongodb.uri");
            String mongoUri = mongoUriRaw != null
                    ? mongoUriRaw.replaceAll("://[^:]+:([^@]+)@", "://*****:*****@")
                    : "not set";
            logger.info("MongoDB URI: {}", mongoUri);

            // Test MongoDB connection
            String dbName = mongoTemplate.getDb().getName();
            logger.info("Successfully connected to MongoDB database: {}", dbName);

            // Write a test document to verify write permissions
            Map<String, Object> testDoc = new HashMap<>();
            testDoc.put("applicationStartup", true);
            testDoc.put("timestamp", System.currentTimeMillis());
            testDoc.put("profiles", Arrays.toString(activeProfiles));

            mongoTemplate.save(testDoc, "application_startup");
            logger.info("Successfully wrote test document to MongoDB");

        } catch (Exception e) {
            logger.error("Failed to connect to MongoDB: {}", e.getMessage(), e);
        }
    }
}