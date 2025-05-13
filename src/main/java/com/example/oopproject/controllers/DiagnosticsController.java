package com.example.oopproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/diagnostics")
public class DiagnosticsController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private Environment environment;

    @GetMapping("/mongodb-test")
    public ResponseEntity<Map<String, Object>> testMongoDbConnection() {
        Map<String, Object> result = new HashMap<>();
        result.put("timestamp", new Date());
        result.put("activeProfiles", Arrays.asList(environment.getActiveProfiles()));

        try {
            // Test MongoDB connection
            String dbName = mongoTemplate.getDb().getName();
            result.put("database", dbName);
            result.put("collections", mongoTemplate.getCollectionNames());
            result.put("status", "connected");

            // Write test document
            Map<String, Object> testDoc = new HashMap<>();
            testDoc.put("test", true);
            testDoc.put("timestamp", System.currentTimeMillis());
            testDoc.put("message", "Connection test");
            Map<String, Object> savedDoc = mongoTemplate.save(testDoc, "diagnostics_tests");
            result.put("testWrite", savedDoc);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("errorMessage", e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/env-info")
    public ResponseEntity<Map<String, Object>> getEnvironmentInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("activeProfiles", Arrays.asList(environment.getActiveProfiles()));
        info.put("javaVersion", System.getProperty("java.version"));
        info.put("osName", System.getProperty("os.name"));

        // Safely log MongoDB URI (mask password)
        String mongoUriRaw = environment.getProperty("MONGODB_URI");
        if (mongoUriRaw != null) {
            String maskedUri = mongoUriRaw.replaceAll("://[^:]+:([^@]+)@", "://*****:*****@");
            info.put("MONGODB_URI", maskedUri);
        } else {
            info.put("MONGODB_URI", "not set");
        }

        return ResponseEntity.ok(info);
    }
}