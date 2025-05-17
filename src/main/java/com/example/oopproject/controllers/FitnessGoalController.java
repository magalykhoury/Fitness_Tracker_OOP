package com.example.oopproject.controllers;

import com.example.oopproject.models.FitnessGoal;
import com.example.oopproject.services.FitnessGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing FitnessGoal entities.
 * Provides endpoints for CRUD operations, pagination, and filtering by user, goal type, and status.
 */
@RestController
@RequestMapping("/api/fitness-goals")
public class FitnessGoalController {

    private final FitnessGoalService fitnessGoalService;

    @Autowired
    public FitnessGoalController(FitnessGoalService fitnessGoalService) {
        this.fitnessGoalService = fitnessGoalService;
    }

    /**
     * Retrieve all fitness goals.
     *
     * @return List of all FitnessGoal objects
     */
    @GetMapping
    public ResponseEntity<List<FitnessGoal>> getAllFitnessGoals() {
        List<FitnessGoal> fitnessGoals = fitnessGoalService.getAllFitnessGoals();
        return new ResponseEntity<>(fitnessGoals, HttpStatus.OK);
    }

    /**
     * Retrieve paginated and sorted fitness goals.
     *
     * @param page Page number (0-based)
     * @param size Page size
     * @param sort Sort field (default "targetDate")
     * @return Page of FitnessGoal objects
     */
    @GetMapping("/paged")
    public ResponseEntity<Page<FitnessGoal>> getPagedFitnessGoals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "targetDate") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<FitnessGoal> fitnessGoals = fitnessGoalService.getAllFitnessGoals(pageable);
        return new ResponseEntity<>(fitnessGoals, HttpStatus.OK);
    }

    /**
     * Retrieve a fitness goal by its ID.
     *
     * @param id FitnessGoal ID
     * @return FitnessGoal object
     */
    @GetMapping("/{id}")
    public ResponseEntity<FitnessGoal> getFitnessGoalById(@PathVariable String id) {
        FitnessGoal fitnessGoal = fitnessGoalService.getFitnessGoalById(id);
        return new ResponseEntity<>(fitnessGoal, HttpStatus.OK);
    }

    /**
     * Retrieve all fitness goals for a specific user.
     *
     * @param userId User ID
     * @return List of FitnessGoal objects for the user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FitnessGoal>> getFitnessGoalsByUserId(@PathVariable String userId) {
        List<FitnessGoal> fitnessGoals = fitnessGoalService.getFitnessGoalsByUserId(userId);
        return new ResponseEntity<>(fitnessGoals, HttpStatus.OK);
    }

    /**
     * Retrieve fitness goals by user ID and goal type.
     *
     * @param userId   User ID
     * @param goalType Goal type (e.g., weight loss, strength)
     * @return List of FitnessGoal objects matching criteria
     */
    @GetMapping("/user/{userId}/type/{goalType}")
    public ResponseEntity<List<FitnessGoal>> getFitnessGoalsByUserIdAndType(
            @PathVariable String userId,
            @PathVariable String goalType) {

        List<FitnessGoal> fitnessGoals = fitnessGoalService.getFitnessGoalsByUserIdAndType(userId, goalType);
        return new ResponseEntity<>(fitnessGoals, HttpStatus.OK);
    }

    /**
     * Retrieve fitness goals by user ID and status.
     *
     * @param userId User ID
     * @param status Status of goal (e.g., completed, in-progress)
     * @return List of FitnessGoal objects matching criteria
     */
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<FitnessGoal>> getFitnessGoalsByUserIdAndStatus(
            @PathVariable String userId,
            @PathVariable String status) {

        List<FitnessGoal> fitnessGoals = fitnessGoalService.getFitnessGoalsByUserIdAndStatus(userId, status);
        return new ResponseEntity<>(fitnessGoals, HttpStatus.OK);
    }
}
