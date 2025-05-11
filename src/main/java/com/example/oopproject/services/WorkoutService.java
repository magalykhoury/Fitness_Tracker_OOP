package com.example.oopproject.services;


import com.example.oopproject.exceptions.ResourceNotFoundException;
import com.example.oopproject.models.Workout;
import com.example.oopproject.repositories.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    public List<Workout> getAllWorkouts() {
        return workoutRepository.findAll();
    }

    public Page<Workout> getAllWorkouts(Pageable pageable) {
        return workoutRepository.findAll(pageable);
    }

    public Workout getWorkoutById(String id) {
        return workoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with id: " + id));
    }

    public List<Workout> getWorkoutsByUserId(String userId) {
        return workoutRepository.findByUserId(userId);
    }

    public List<Workout> getWorkoutsByType(String workoutType) {
        return workoutRepository.findByWorkoutType(workoutType);
    }

    public List<Workout> getWorkoutsByDateRange(Date startDate, Date endDate) {
        return workoutRepository.findByDateBetween(startDate, endDate);
    }

    public Workout createWorkout(Workout workout) {
        return workoutRepository.save(workout);
    }

    public Workout updateWorkout(String id, Workout workoutDetails) {
        Workout workout = getWorkoutById(id);

        workout.setUserId(workoutDetails.getUserId());
        workout.setDate(workoutDetails.getDate());
        workout.setDuration(workoutDetails.getDuration());
        workout.setWorkoutType(workoutDetails.getWorkoutType());
        workout.setCaloriesBurned(workoutDetails.getCaloriesBurned());
        workout.setExercises(workoutDetails.getExercises());

        return workoutRepository.save(workout);
    }

    public void deleteWorkout(String id) {
        Workout workout = getWorkoutById(id);
        workoutRepository.delete(workout);
    }

    public List<Workout> searchWorkouts(String workoutType, String userId) {
        if (workoutType != null && userId != null) {
            return workoutRepository.findByUserIdAndWorkoutType(userId, workoutType);
        } else if (workoutType != null) {
            return workoutRepository.findByWorkoutType(workoutType);
        } else if (userId != null) {
            return workoutRepository.findByUserId(userId);
        } else {
            return workoutRepository.findAll();
        }
    }
}