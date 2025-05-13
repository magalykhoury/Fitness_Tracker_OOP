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

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    @Autowired
    private WorkoutRepository workoutRepository;

    // CREATE
    @PostMapping
    public Workout createWorkout(@RequestBody Workout workout) {
        // Let MongoDB generate the ObjectId automatically
        // No need to manually set the ID as MongoDB will handle it
        return workoutRepository.save(workout);
    }

    // READ ALL
    @GetMapping
    public List<Workout> getAllWorkouts() {
        return workoutRepository.findAll();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Workout> getWorkoutById(@PathVariable String id) {
        try {
            Workout workout = workoutRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Workout not found with id: " + id));
            return ResponseEntity.ok(workout);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Workout> updateWorkout(@PathVariable String id, @RequestBody Workout updatedWorkout) {
        try {
            Workout workout = workoutRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Workout not found with id: " + id));

            workout.setName(updatedWorkout.getName());
            workout.setDate(updatedWorkout.getDate());
            return ResponseEntity.ok(workoutRepository.save(workout));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable String id) {
        if (workoutRepository.existsById(id)) {
            workoutRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // PAGINATED & SORTED ENDPOINT
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

    // FILTER BY DATE RANGE
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

    // SEARCH BY WORKOUT TYPE (OPTIONAL USER ID)
    @GetMapping("/search")
    public ResponseEntity<List<Workout>> searchWorkouts(
            @RequestParam String workoutType,
            @RequestParam(required = false) String userId) {

        List<Workout> results;
        if (userId != null && !userId.isEmpty()) {
            results = workoutRepository.findByUserIdAndWorkoutType(userId, workoutType);
        } else {
            results = workoutRepository.findByWorkoutType(workoutType);
        }
        return ResponseEntity.ok(results);
    }
}
