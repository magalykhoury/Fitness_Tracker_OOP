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

@RestController
@RequestMapping("/exercises")
public class ExerciseController {

    @Autowired
    private ExerciseRepository exerciseRepository;

    // CREATE
    @PostMapping
    public Exercise createExercise(@RequestBody Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    // READ ALL
    @GetMapping
    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    // READ PAGINATED + SORTED
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

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Exercise> getExerciseById(@PathVariable String id) {
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        return exercise.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public Exercise updateExercise(@PathVariable String id, @RequestBody Exercise updatedExercise) {
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
    public ResponseEntity<Void> deleteExercise(@PathVariable String id) {
        if (exerciseRepository.existsById(id)) {
            exerciseRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // FILTERED AND SEARCHED RESULTS
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
