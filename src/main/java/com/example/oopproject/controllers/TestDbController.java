package com.example.oopproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestDbController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/db-info")
    public ResponseEntity<Map<String, Object>> getDatabaseInfo() {
        Map<String, Object> info = new HashMap<>();

        try {
            // Get database info
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

    @GetMapping("/write-test")
    public ResponseEntity<Map<String, Object>> writeTestDocument() {
        Map<String, Object> result = new HashMap<>();

        try {
            // Create a test document
            Map<String, Object> testDoc = new HashMap<>();
            testDoc.put("test", true);
            testDoc.put("timestamp", System.currentTimeMillis());
            testDoc.put("message", "Test document from Render deployment");

            // Save to MongoDB
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