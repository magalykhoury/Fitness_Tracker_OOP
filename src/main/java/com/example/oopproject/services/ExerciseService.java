package com.example.oopproject.services;

import com.example.oopproject.models.Exercise;
import com.example.oopproject.repositories.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public Exercise createExercise(Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    // New methods below

    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    public Exercise updateExercise(String id, Exercise updatedExercise) {
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

    public void deleteExercise(String id) {
        if (exerciseRepository.existsById(id)) {
            exerciseRepository.deleteById(id);
        }
    }

    public Exercise getExerciseById(String id) {
        return exerciseRepository.findById(id).orElse(null);
    }
}
