package com.example.oopproject.repositories;

import com.example.oopproject.models.Workout;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Repository interface for managing Workout entities in MongoDB.
 * Provides CRUD operations and custom query methods.
 */
@Repository
public interface WorkoutRepository extends MongoRepository<Workout, String> {

    /**
     * Finds all workouts by a specific user ID.
     *
     * @param userId the ID of the user
     * @return a list of workouts for the given user
     */
    List<Workout> findByUserId(String userId);

    /**
     * Finds all workouts of a specific type.
     *
     * @param workoutType the type of the workout (e.g., "cardio")
     * @return a list of workouts matching the type
     */
    List<Workout> findByWorkoutType(String workoutType);

    /**
     * Finds workouts that occurred between two dates.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @return a list of workouts between the specified dates
     */
    List<Workout> findByDateBetween(Date startDate, Date endDate);

    /**
     * Finds workouts for a user filtered by workout type.
     *
     * @param userId the user ID
     * @param workoutType the type of workout
     * @return a list of workouts matching user and type
     */
    List<Workout> findByUserIdAndWorkoutType(String userId, String workoutType);

    /**
     * Finds the most recently added workout based on descending ID order.
     *
     * @return the latest workout entry
     */
    List<Workout> findTopByOrderByIdDesc();
}
