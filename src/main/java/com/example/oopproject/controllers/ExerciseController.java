package com.example.oopproject.controllers;

import com.example.oopproject.models.Exercise;
import com.example.oopproject.repositories.ExerciseRepository;
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

/**
 * REST controller for managing Exercise entities.
 * Provides endpoints to create, read, update, delete, paginate, sort, and filter exercises.
 */
@RestController
@RequestMapping("/exercises")
public class ExerciseController {

    @Autowired
    private ExerciseRepository exerciseRepository;

    /**
     * Create a new exercise.
     *
     * @param exercise Exercise object to create
     * @return Created Exercise
     */
    @PostMapping
    public Exercise createExercise(@RequestBody Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    /**
     * Retrieve all exercises.
     *
     * @return List of all Exercise objects
     */
    @GetMapping
    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    /**
     * Retrieve paginated and sorted exercises.
     *
     * @param page      Page number (0-based)
     * @param size      Page size
     * @param sortBy    Field to sort by
     * @param direction Sort direction ("asc" or "desc")
     * @return Page of Exercise objects
     */
    @GetMapping("/paginated")
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

    /**
     * Retrieve an exercise by its ID.
     *
     * @param id Exercise ID
     * @return ResponseEntity containing Exercise if found, or 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Exercise> getExerciseById(@PathVariable String id) {
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        return exercise.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Update an existing exercise by ID, or create it if not present.
     *
     * @param id              Exercise ID
     * @param updatedExercise  Exercise object with updated data
     * @return Updated or newly created Exercise
     */
    @PutMapping("/{id}")
    public Exercise updateExercise(@PathVariable String id, @RequestBody Exercise updatedExercise) {
        return exerciseRepository.findById(id)
                .map(exercise -> {
                    exercise.setName(updatedExercise.getName());
                    exercise.setReps(updatedExercise.getReps());
                    exercise.setSets(updatedExercise.getSets());
                    exercise.setWorkoutId(updatedExercise.getWorkoutId());
                    // Added from first code: weight, equipment, muscleGroups
                    exercise.setWeight(updatedExercise.getWeight());
                    exercise.setEquipment(updatedExercise.getEquipment());
                    exercise.setMuscleGroups(updatedExercise.getMuscleGroups());
                    return exerciseRepository.save(exercise);
                })
                .orElseGet(() -> {
                    updatedExercise.setId(id);
                    return exerciseRepository.save(updatedExercise);
                });
    }

    /**
     * Delete an exercise by its ID.
     *
     * @param id Exercise ID
     * @return ResponseEntity with status NO_CONTENT if deleted, or NOT_FOUND if not present
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable String id) {
        if (exerciseRepository.existsById(id)) {
            exerciseRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Retrieve exercises filtered by optional criteria: reps, sets, name, and workoutId.
     *
     * @param reps      Optional filter for number of repetitions
     * @param sets      Optional filter for number of sets
     * @param name      Optional filter for exercise name (partial, case-insensitive)
     * @param workoutId Optional filter for associated workout ID
     * @return ResponseEntity containing list of filtered exercises
     */
    @GetMapping("/filter")
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

