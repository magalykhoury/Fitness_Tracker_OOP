package com.example.oopproject.repositories;

import com.example.oopproject.models.Workout;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WorkoutRepository extends MongoRepository<Workout, String> {
    List<Workout> findByUserId(String userId);
    List<Workout> findByWorkoutType(String workoutType);
    List<Workout> findByDateBetween(Date startDate, Date endDate);
    List<Workout> findByUserIdAndWorkoutType(String userId, String workoutType);
}