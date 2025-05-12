package com.example.oopproject.controllers;

import com.example.oopproject.models.Exercise;
import com.example.oopproject.models.Workout;
import com.example.oopproject.services.ExerciseService;
import com.example.oopproject.services.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private WorkoutService workoutService;

    @Autowired
    private ExerciseService exerciseService;

    // WORKOUTS
    @PostMapping("/workouts")
    public Workout createWorkout(@RequestBody Workout workout) {
        return workoutService.createWorkout(workout);
    }

    @PutMapping("/workouts/{id}")
    public Workout updateWorkout(@PathVariable String id, @RequestBody Workout workout) {
        return workoutService.updateWorkout(id, workout);
    }

    @DeleteMapping("/workouts/{id}")
    public void deleteWorkout(@PathVariable String id) {
        workoutService.deleteWorkout(id);
    }

    @GetMapping("/workouts")
    public List<Workout> getAllWorkouts() {
        return workoutService.getAllWorkouts();
    }

    // EXERCISES
    @PostMapping("/exercises")
    public Exercise createExercise(@RequestBody Exercise exercise) {
        return exerciseService.createExercise(exercise);
    }

    @PutMapping("/exercises/{id}")
    public Exercise updateExercise(@PathVariable String id, @RequestBody Exercise exercise) {
        return exerciseService.updateExercise(id, exercise);
    }

    @DeleteMapping("/exercises/{id}")
    public void deleteExercise(@PathVariable String id) {
        exerciseService.deleteExercise(id);
    }

    @GetMapping("/exercises")
    public List<Exercise> getAllExercises() {
        return exerciseService.getAllExercises();
    }
}

