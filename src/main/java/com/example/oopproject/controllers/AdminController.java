package com.example.oopproject.controllers;

import com.example.oopproject.models.Exercise;
import com.example.oopproject.models.Workout;
import com.example.oopproject.services.ExerciseService;
import com.example.oopproject.services.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for admin-related operations on workouts and exercises.
 * Provides endpoints to create, update, delete, and retrieve workouts and exercises.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private WorkoutService workoutService;

    @Autowired
    private ExerciseService exerciseService;

    // WORKOUTS

    /**
     * Creates a new workout.
     * @param workout the workout data to create
     * @return the created Workout object
     */
    @PostMapping("/workouts")
    public Workout createWorkout(@RequestBody Workout workout) {
        return workoutService.createWorkout(workout);
    }

    /**
     * Updates an existing workout identified by its ID.
     * @param id the ID of the workout to update
     * @param workout the updated workout data
     * @return the updated Workout object
     */
    @PutMapping("/workouts/{id}")
    public Workout updateWorkout(@PathVariable String id, @RequestBody Workout workout) {
        return workoutService.updateWorkout(id, workout);
    }

    /**
     * Deletes a workout by its ID.
     * @param id the ID of the workout to delete
     */
    @DeleteMapping("/workouts/{id}")
    public void deleteWorkout(@PathVariable String id) {
        workoutService.deleteWorkout(id);
    }

    /**
     * Retrieves a list of all workouts.
     * @return list of Workout objects
     */
    @GetMapping("/workouts")
    public List<Workout> getAllWorkouts() {
        return workoutService.getAllWorkouts();
    }

    // EXERCISES

    /**
     * Creates a new exercise.
     * @param exercise the exercise data to create
     * @return the created Exercise object
     */
    @PostMapping("/exercises")
    public Exercise createExercise(@RequestBody Exercise exercise) {
        return exerciseService.createExercise(exercise);
    }

    /**
     * Updates an existing exercise identified by its ID.
     * @param id the ID of the exercise to update
     * @param exercise the updated exercise data
     * @return the updated Exercise object
     */
    @PutMapping("/exercises/{id}")
    public Exercise updateExercise(@PathVariable String id, @RequestBody Exercise exercise) {
        return exerciseService.updateExercise(id, exercise);
    }

    /**
     * Deletes an exercise by its ID.
     * @param id the ID of the exercise to delete
     */
    @DeleteMapping("/exercises/{id}")
    public void deleteExercise(@PathVariable String id) {
        exerciseService.deleteExercise(id);
    }

    /**
     * Retrieves a list of all exercises.
     * @return list of Exercise objects
     */
    @GetMapping("/exercises")
    public List<Exercise> getAllExercises() {
        return exerciseService.getAllExercises();
    }
}
