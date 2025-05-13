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
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Profile({"prod", "cloud"})
public class ProdMongoConfig {

    private static final Logger logger = LoggerFactory.getLogger(ProdMongoConfig.class);

    @Value("${MONGODB_URI}")
    private String mongoUri;

    @Bean
    public MongoClient mongoClient() {
        logger.info("Initializing MongoDB client for production environment...");
        logger.info("Using MongoDB Atlas");

        // For security, don't log the full connection string
        logger.debug("MongoDB URI starts with: {}",
                mongoUri != null ? mongoUri.substring(0, Math.min(20, mongoUri.length())) + "..." : "null");

        if (mongoUri == null || mongoUri.isEmpty()) {
            throw new IllegalStateException("MongoDB URI is not properly configured");
        }

        ConnectionString connectionString = new ConnectionString(mongoUri);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        logger.info("Creating MongoTemplate for production environment...");
        // Extract database name from the connection URI or use a default
        String databaseName = "fitness_tracker";
        ConnectionString connectionString = new ConnectionString(mongoUri);
        if (connectionString.getDatabase() != null) {
            databaseName = connectionString.getDatabase();
        }
        logger.info("Using database: {}", databaseName);

        return new MongoTemplate(mongoClient(), databaseName);
    }
}