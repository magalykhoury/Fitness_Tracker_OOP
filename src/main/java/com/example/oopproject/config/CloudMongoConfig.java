package com.example.oopproject.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Profile("cloud")
public class CloudMongoConfig {

    private static final Logger logger = LoggerFactory.getLogger(CloudMongoConfig.class);

    @Value("${MONGODB_URI:mongodb+srv://magaly:mohamad@cluster0.ientrf8.mongodb.net/fitness_tracker?retryWrites=true&w=majority}")
    private String mongoUri;

    @Bean
    @Primary
    public MongoClient mongoClient() {
        logger.info("Initializing MongoDB client for cloud environment...");
        logger.info("Using MongoDB URI: {}", mongoUri.replaceAll("://[^:]+:([^@]+)@", "://*****:*****@"));

        ConnectionString connectionString = new ConnectionString(mongoUri);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    @Primary
    public MongoTemplate mongoTemplate() throws Exception {
        logger.info("Creating MongoTemplate for cloud environment...");
        return new MongoTemplate(mongoClient(), "fitness_tracker");
    }
}