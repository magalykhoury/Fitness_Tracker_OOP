package com.example.oopproject.controllers;

import com.example.oopproject.models.Exercise;
import com.example.oopproject.repositories.ExerciseRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/exercises")
@Tag(name = "Exercise Management", description = "APIs for managing exercises")
public class ExerciseController {

    @Autowired
    private ExerciseRepository exerciseRepository;

    // CREATE
    @PostMapping
    @Operation(summary = "Create a new exercise", description = "Creates a new exercise with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exercise created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Exercise.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public Exercise createExercise(
            @Parameter(description = "Exercise to be created", required = true, schema = @Schema(implementation = Exercise.class))
            @RequestBody Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    // READ ALL
    @GetMapping
    @Operation(summary = "Get all exercises", description = "Retrieves a list of all exercises")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all exercises",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Exercise.class)))
    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    // READ PAGINATED + SORTED
    @GetMapping("/paginated")
    @Operation(summary = "Get paginated and sorted exercises", description = "Retrieves exercises with pagination and sorting options")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved paginated exercises",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Exercise.class)))
    public Page<Exercise> getExercisesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return exerciseRepository.findAll(pageable);
    }

    // READ BY ID
    @GetMapping("/{id}")
    @Operation(summary = "Get exercise by ID", description = "Retrieves an exercise by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exercise found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Exercise.class))),
            @ApiResponse(responseCode = "404", description = "Exercise not found")
    })
    public ResponseEntity<Exercise> getExerciseById(
            @Parameter(description = "ID of the exercise to be retrieved", required = true)
            @PathVariable String id) {
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        return exercise.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    @Operation(summary = "Update an exercise", description = "Updates an exercise with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exercise updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Exercise.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public Exercise updateExercise(
            @Parameter(description = "ID of the exercise to be updated", required = true)
            @PathVariable String id,
            @Parameter(description = "Updated exercise information", required = true, schema = @Schema(implementation = Exercise.class))
            @RequestBody Exercise updatedExercise) {
        return exerciseRepository.findById(id)
                .map(exercise -> {
                    exercise.setName(updatedExercise.getName());
                    exercise.setReps(updatedExercise.getReps());
                    exercise.setSets(updatedExercise.getSets());
                    exercise.setWorkoutId(updatedExercise.getWorkoutId());
                    return exerciseRepository.save(exercise);
                })
                .orElseGet(() -> {
                    updatedExercise.setId(id);
                    return exerciseRepository.save(updatedExercise);
                });
    }

    // DELETE
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an exercise", description = "Deletes an exercise with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Exercise deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Exercise not found")
    })
    public ResponseEntity<Void> deleteExercise(
            @Parameter(description = "ID of the exercise to be deleted", required = true)
            @PathVariable String id) {
        if (exerciseRepository.existsById(id)) {
            exerciseRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // FILTERED AND SEARCHED RESULTS
    @GetMapping("/filter")
    @Operation(summary = "Filter exercises", description = "Filters exercises based on reps, sets, name, and workoutId")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved filtered exercises",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Exercise.class)))
    public ResponseEntity<List<Exercise>> getExercisesByFilter(
            @RequestParam(required = false) Integer reps,
            @RequestParam(required = false) Integer sets,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String workoutId
    ) {
        List<Exercise> allExercises = exerciseRepository.findAll();
        List<Exercise> filtered = allExercises.stream()
                .filter(e -> (reps == null || e.getReps() == reps))
                .filter(e -> (sets == null || e.getSets() == sets))
                .filter(e -> (!StringUtils.hasText(name) || e.getName().toLowerCase().contains(name.toLowerCase())))
                .filter(e -> (!StringUtils.hasText(workoutId) || e.getWorkoutId().equals(workoutId)))
                .toList();

        return new ResponseEntity<>(filtered, HttpStatus.OK);
    }
}
