package com.example.oopproject.controllers;


import com.example.oopproject.models.FitnessGoal;
import com.example.oopproject.services.FitnessGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fitness-goals")
public class FitnessGoalController {

    private final FitnessGoalService fitnessGoalService;

    @Autowired
    public FitnessGoalController(FitnessGoalService fitnessGoalService) {
        this.fitnessGoalService = fitnessGoalService;
    }

    @GetMapping
    public ResponseEntity<List<FitnessGoal>> getAllFitnessGoals() {
        List<FitnessGoal> fitnessGoals = fitnessGoalService.getAllFitnessGoals();
        return new ResponseEntity<>(fitnessGoals, HttpStatus.OK);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<FitnessGoal>> getPagedFitnessGoals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "targetDate") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<FitnessGoal> fitnessGoals = fitnessGoalService.getAllFitnessGoals(pageable);
        return new ResponseEntity<>(fitnessGoals, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FitnessGoal> getFitnessGoalById(@PathVariable String id) {
        FitnessGoal fitnessGoal = fitnessGoalService.getFitnessGoalById(id);
        return new ResponseEntity<>(fitnessGoal, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FitnessGoal>> getFitnessGoalsByUserId(@PathVariable String userId) {
        List<FitnessGoal> fitnessGoals = fitnessGoalService.getFitnessGoalsByUserId(userId);
        return new ResponseEntity<>(fitnessGoals, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/type/{goalType}")
    public ResponseEntity<List<FitnessGoal>> getFitnessGoalsByUserIdAndType(
            @PathVariable String userId,
            @PathVariable String goalType) {

        List<FitnessGoal> fitnessGoals = fitnessGoalService.getFitnessGoalsByUserIdAndType(userId, goalType);
        return new ResponseEntity<>(fitnessGoals, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<FitnessGoal>> getFitnessGoalsByUserIdAndStatus(
            @PathVariable String userId,
            @PathVariable String status) {

        List<FitnessGoal> fitnessGoals = fitnessGoalService.getFitnessGoalsByUserIdAndStatus(userId, status);
        return new ResponseEntity<>(fitnessGoals, HttpStatus.OK);
    }
}