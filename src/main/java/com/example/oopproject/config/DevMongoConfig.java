package com.example.oopproject.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Profile("dev")
public class DevMongoConfig {

    private static final Logger logger = LoggerFactory.getLogger(DevMongoConfig.class);

    @Bean
    public MongoClient mongoClient() {
        logger.info("Initializing MongoDB client for development environment...");
        logger.info("Using local MongoDB at mongodb://localhost:27017");

        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        logger.info("Creating MongoTemplate for development environment...");
        return new MongoTemplate(mongoClient(), "fitness_tracker");
    }
}