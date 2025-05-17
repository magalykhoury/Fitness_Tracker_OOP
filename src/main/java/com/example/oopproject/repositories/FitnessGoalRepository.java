package com.example.oopproject.repositories;

import com.example.oopproject.models.FitnessGoal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Repository interface for managing FitnessGoal entities in MongoDB.
 * Extends MongoRepository to provide CRUD operations.
 */
@Repository
public interface FitnessGoalRepository extends MongoRepository<FitnessGoal, String> {

    /**
     * Finds all fitness goals for a given user.
     *
     * @param userId the ID of the user
     * @return a list of fitness goals belonging to the user
     */
    List<FitnessGoal> findByUserId(String userId);

    /**
     * Finds fitness goals for a user filtered by goal type.
     *
     * @param userId the ID of the user
     * @param goalType the type of goal (e.g., "weight loss", "strength")
     * @return a list of fitness goals matching the user and goal type
     */
    List<FitnessGoal> findByUserIdAndGoalType(String userId, String goalType);

    /**
     * Finds fitness goals for a user filtered by status.
     *
     * @param userId the ID of the user
     * @param status the status of the goal (e.g., "active", "completed")
     * @return a list of fitness goals matching the user and status
     */
    List<FitnessGoal> findByUserIdAndStatus(String userId, String status);

    /**
     * Finds fitness goals with a target date before the specified date.
     *
     * @param date the cutoff date
     * @return a list of fitness goals with target dates before the given date
     */
    List<FitnessGoal> findByTargetDateBefore(Date date);
}
