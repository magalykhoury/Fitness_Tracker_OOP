package com.example.oopproject.controllers;

import com.example.oopproject.models.Workout;
import com.example.oopproject.repositories.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * REST Controller for managing Workout entities.
 */
@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    @Autowired
    private WorkoutRepository workoutRepository;

    /**
     * Create a new workout.
     */
    @PostMapping
    public ResponseEntity<Workout> createWorkout(@RequestBody Workout workout) {
        Workout savedWorkout = workoutRepository.save(workout);
        return new ResponseEntity<>(savedWorkout, HttpStatus.CREATED);
    }

    /**
     * Get all workouts.
     */
    @GetMapping
    public ResponseEntity<List<Workout>> getAllWorkouts() {
        return ResponseEntity.ok(workoutRepository.findAll());
    }

    /**
     * Get a workout by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Workout> getWorkoutById(@PathVariable String id) {
        return workoutRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Update a workout by ID.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Workout> updateWorkout(@PathVariable String id, @RequestBody Workout updatedWorkout) {
        return workoutRepository.findById(id)
                .map(existingWorkout -> {
                    existingWorkout.setName(updatedWorkout.getName());
                    existingWorkout.setDate(updatedWorkout.getDate());
                    existingWorkout.setDuration(updatedWorkout.getDuration());
                    existingWorkout.setCaloriesBurned(updatedWorkout.getCaloriesBurned());
                    existingWorkout.setWorkoutType(updatedWorkout.getWorkoutType());
                    Workout saved = workoutRepository.save(existingWorkout);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Delete a workout by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable String id) {
        if (workoutRepository.existsById(id)) {
            workoutRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get paginated and sorted workouts.
     */
    @GetMapping("/paginated")
    public ResponseEntity<List<Workout>> getPaginatedWorkouts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Workout> workoutPage = workoutRepository.findAll(pageable);
        return ResponseEntity.ok(workoutPage.getContent());
    }

    /**
     * Filter workouts by a date range.
     */
    @GetMapping("/filterByDate")
    public ResponseEntity<List<Workout>> filterByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date start = formatter.parse(startDate);
            Date end = formatter.parse(endDate);
            List<Workout> workouts = workoutRepository.findByDateBetween(start, end);
            return ResponseEntity.ok(workouts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Search workouts by workout type (and optional userId).
     */
    @GetMapping("/search")
    public ResponseEntity<List<Workout>> searchWorkouts(
            @RequestParam String workoutType,
            @RequestParam(required = false) String userId) {

        List<Workout> results = (userId != null && !userId.isEmpty())
                ? workoutRepository.findByUserIdAndWorkoutType(userId, workoutType)
                : workoutRepository.findByWorkoutType(workoutType);

        return ResponseEntity.ok(results);
    }
}
