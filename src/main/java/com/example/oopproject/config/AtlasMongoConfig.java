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

/**
 * Configuration class for connecting to MongoDB Atlas in the cloud.
 *
 * <p>This configuration is active only when the "cloud-direct" Spring profile is enabled.</p>
 *
 * <p>Provides a {@link MongoClient} bean and a {@link MongoTemplate} bean for accessing
 * the fitness_tracker database hosted on MongoDB Atlas.</p>
 */
@Configuration
@Profile("cloud-direct")
public class AtlasMongoConfig {

    private static final Logger logger = LoggerFactory.getLogger(AtlasMongoConfig.class);

    /**
     * The connection string URI for MongoDB Atlas.
     * Replace the username, password, and cluster details accordingly.
     */
    private static final String MONGO_URI =
            "mongodb+srv://magaly:mohamad@cluster0.ientrf8.mongodb.net/fitness_tracker?retryWrites=true&w=majority";

    /**
     * Creates and returns a {@link MongoClient} connected to the MongoDB Atlas cluster.
     *
     * @return a {@link MongoClient} instance for database operations
     */
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

    /**
     * Creates a {@link MongoTemplate} bean for performing MongoDB operations on the
     * "fitness_tracker" database.
     *
     * @return a {@link MongoTemplate} instance
     */
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
