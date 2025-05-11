package com.example.oopproject.controllers;

import com.example.oopproject.models.Workout;
import com.example.oopproject.repositories.WorkoutRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Workout Management", description = "APIs for managing workouts")
public class WorkoutController {

    @Autowired
    private WorkoutRepository workoutRepository;

    // CREATE
    @PostMapping
    @Operation(summary = "Create a new workout", description = "Creates a new workout with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workout created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Workout.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public Workout createWorkout(
            @Parameter(description = "Workout to be created", required = true, schema = @Schema(implementation = Workout.class))
            @RequestBody Workout workout) {
        return workoutRepository.save(workout);
    }

    // READ ALL
    @GetMapping
    @Operation(summary = "Get all workouts", description = "Retrieves a list of all workouts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all workouts",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Workout.class)))
    })
    public List<Workout> getAllWorkouts() {
        return workoutRepository.findAll();
    }

    // READ BY ID
    @GetMapping("/{id}")
    @Operation(summary = "Get workout by ID", description = "Retrieves a workout by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workout found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Workout.class))),
            @ApiResponse(responseCode = "404", description = "Workout not found")
    })
    public ResponseEntity<Workout> getWorkoutById(
            @Parameter(description = "ID of the workout to be retrieved", required = true)
            @PathVariable String id) {
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
    @Operation(summary = "Update a workout", description = "Updates a workout with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workout updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Workout.class))),
            @ApiResponse(responseCode = "404", description = "Workout not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<Workout> updateWorkout(
            @Parameter(description = "ID of the workout to be updated", required = true)
            @PathVariable String id,
            @Parameter(description = "Updated workout information", required = true, schema = @Schema(implementation = Workout.class))
            @RequestBody Workout updatedWorkout) {
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
    @Operation(summary = "Delete a workout", description = "Deletes a workout with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Workout deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Workout not found")
    })
    public ResponseEntity<Void> deleteWorkout(
            @Parameter(description = "ID of the workout to be deleted", required = true)
            @PathVariable String id) {
        if (workoutRepository.existsById(id)) {
            workoutRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // NEW PAGINATED & SORTED ENDPOINT
    @GetMapping("/paginated")
    @Operation(summary = "Get paginated and sorted workouts", description = "Retrieves workouts with pagination and optional sorting by field and direction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved paginated and sorted workouts",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Workout.class)))
    })
    public ResponseEntity<List<Workout>> getPaginatedWorkouts(
            @Parameter(description = "Page number (zero-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field (e.g., name, date)") @RequestParam(defaultValue = "date") String sortBy,
            @Parameter(description = "Sort direction (asc or desc)") @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Workout> workoutPage = workoutRepository.findAll(pageable);
        return ResponseEntity.ok(workoutPage.getContent());
    }

    // ✅ FILTER BY DATE RANGE
    @GetMapping("/filterByDate")
    @Operation(summary = "Filter workouts by date range", description = "Retrieves workouts between startDate and endDate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved workouts in date range",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Workout.class))),
            @ApiResponse(responseCode = "400", description = "Invalid date format")
    })
    public ResponseEntity<List<Workout>> filterByDateRange(
            @Parameter(description = "Start date in format yyyy-MM-dd") @RequestParam String startDate,
            @Parameter(description = "End date in format yyyy-MM-dd") @RequestParam String endDate) {
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

    // ✅ SEARCH BY WORKOUT TYPE (OPTIONAL USER ID)
    @GetMapping("/search")
    @Operation(summary = "Search workouts by workoutType and optional userId", description = "Retrieves workouts by type and optionally filtered by user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved workouts matching criteria",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Workout.class)))
    })
    public ResponseEntity<List<Workout>> searchWorkouts(
            @Parameter(description = "Workout type to search for") @RequestParam String workoutType,
            @Parameter(description = "User ID (optional)") @RequestParam(required = false) String userId) {

        List<Workout> results;
        if (userId != null && !userId.isEmpty()) {
            results = workoutRepository.findByUserIdAndWorkoutType(userId, workoutType);
        } else {
            results = workoutRepository.findByWorkoutType(workoutType);
        }
        return ResponseEntity.ok(results);
    }
}
