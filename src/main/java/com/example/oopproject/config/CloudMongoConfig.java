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

/**
 * Configuration class for MongoDB connection in the cloud environment.
 *
 * <p>This configuration is active when the "cloud" Spring profile is enabled.</p>
 *
 * <p>Uses the MongoDB URI provided via the environment variable {@code MONGODB_URI},
 * with a default fallback connection string if none is provided.</p>
 */
@Configuration
@Profile("cloud")
public class CloudMongoConfig {

    private static final Logger logger = LoggerFactory.getLogger(CloudMongoConfig.class);

    /**
     * MongoDB connection URI, injected from environment variable or default value.
     */
    @Value("${MONGODB_URI:mongodb+srv://magaly:mohamad@cluster0.ientrf8.mongodb.net/fitness_tracker?retryWrites=true&w=majority}")
    private String mongoUri;

    /**
     * Creates and returns a {@link MongoClient} configured with the connection URI.
     *
     * @return {@link MongoClient} instance for cloud MongoDB access
     */
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

    /**
     * Creates and returns a {@link MongoTemplate} to interact with the
     * "fitness_tracker" database.
     *
     * @return {@link MongoTemplate} instance for database operations
     * @throws Exception if there is an error creating the template
     */
    @Bean
    @Primary
    public MongoTemplate mongoTemplate() throws Exception {
        logger.info("Creating MongoTemplate for cloud environment...");
        return new MongoTemplate(mongoClient(), "fitness_tracker");
    }
}
