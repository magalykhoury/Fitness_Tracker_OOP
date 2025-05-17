package com.example.oopproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller to test MongoDB connection and basic operations.
 */
@RestController
@RequestMapping("/api/test")
public class TestDbController {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Endpoint to fetch database info: name, collections, and connection status.
     * @return JSON containing database info or error message.
     */
    @GetMapping("/db-info")
    public ResponseEntity<Map<String, Object>> getDatabaseInfo() {
        Map<String, Object> info = new HashMap<>();

        try {
            String databaseName = mongoTemplate.getDb().getName();
            info.put("databaseName", databaseName);
            info.put("collections", mongoTemplate.getCollectionNames());
            info.put("status", "connected");
            return ResponseEntity.ok(info);
        } catch (Exception e) {
            info.put("status", "error");
            info.put("message", e.getMessage());
            return ResponseEntity.status(500).body(info);
        }
    }

    /**
     * Endpoint to write a test document to MongoDB to verify write permissions.
     * @return JSON confirming success or error details.
     */
    @GetMapping("/write-test")
    public ResponseEntity<Map<String, Object>> writeTestDocument() {
        Map<String, Object> result = new HashMap<>();

        try {
            Map<String, Object> testDoc = new HashMap<>();
            testDoc.put("test", true);
            testDoc.put("timestamp", System.currentTimeMillis());
            testDoc.put("message", "Test document from Render deployment");

            Map savedDoc = mongoTemplate.save(testDoc, "test_documents");

            result.put("success", true);
            result.put("document", savedDoc);
            result.put("database", mongoTemplate.getDb().getName());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }
}
