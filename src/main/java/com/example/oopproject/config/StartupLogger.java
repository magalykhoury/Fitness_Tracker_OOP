package com.example.oopproject.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@Configuration
public class StartupLogger {

    private static final Logger logger = LoggerFactory.getLogger(StartupLogger.class);

    @Value("${spring.data.mongodb.uri:NOT_SET}")
    private String mongoDbUri;

    /**
     * Bean that runs on application startup to log important environment and configuration information.
     *
     * Logs active Spring profiles, the MongoDB URI with sensitive information masked,
     * and whether MongoDB Atlas is being used.
     *
     * @param environment the Spring Environment to get active profiles
     * @return CommandLineRunner that executes logging at startup
     */
    @Bean
    public CommandLineRunner logStartupInfo(Environment environment) {
        return args -> {
            logger.info("-------- APPLICATION STARTUP INFO --------");
            logger.info("Active profiles: {}", Arrays.toString(environment.getActiveProfiles()));

            // Safely log MongoDB URI (mask password)
            String redactedUri = mongoDbUri.replaceAll("://[^:]+:([^@]+)@", "://*****:*****@");
            logger.info("MongoDB URI: {}", redactedUri);

            // Check if we're using MongoDB Atlas by inspecting the URI
            boolean isAtlas = mongoDbUri.contains("mongodb+srv://") ||
                    (mongoDbUri.contains("mongodb://") && !mongoDbUri.contains("localhost"));

            logger.info("Using MongoDB Atlas: {}", isAtlas);
            logger.info("----------------------------------------");
        };
    }
}
