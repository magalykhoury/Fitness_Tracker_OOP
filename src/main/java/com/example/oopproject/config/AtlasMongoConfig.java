package com.example.oopproject.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Profile("cloud-direct")
public class AtlasMongoConfig {

    private static final Logger logger = LoggerFactory.getLogger(AtlasMongoConfig.class);

    // Hardcoded connection string with your new credentials
    private static final String MONGO_URI =
            "mongodb+srv://magaly:mohamad@cluster0.ientrf8.mongodb.net/fitness_tracker?retryWrites=true&w=majority";

    @Bean
    @Primary
    public MongoClient mongoClient() {
        logger.info("Creating MongoDB client with hardcoded Atlas URI");
        try {
            return MongoClients.create(MONGO_URI);
        } catch (Exception e) {
            logger.error("Failed to create MongoDB client: " + e.getMessage(), e);
            throw e;
        }
    }

    @Bean
    @Primary
    public MongoTemplate mongoTemplate() {
        logger.info("Creating MongoDB template for database: fitness_tracker");
        try {
            return new MongoTemplate(mongoClient(), "fitness_tracker");
        } catch (Exception e) {
            logger.error("Failed to create MongoTemplate: " + e.getMessage(), e);
            throw e;
        }
    }
}