package com.example.oopproject.repositories;

import com.example.oopproject.models.Exercise;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Exercise entities in MongoDB.
 * Extends MongoRepository to provide CRUD operations.
 */
@Repository
public interface ExerciseRepository extends MongoRepository<Exercise, String> {

    /**
     * Finds exercises by their exact name.
     *
     * @param name the name of the exercise
     * @return a list of exercises matching the given name
     */
    List<Exercise> findByName(String name);

    /**
     * Finds exercises that target a specific muscle group.
     *
     * @param muscleGroup the muscle group to search for
     * @return a list of exercises containing the specified muscle group
     */
    List<Exercise> findByMuscleGroupsContaining(String muscleGroup);

    /**
     * Finds exercises that require specific equipment.
     *
     * @param equipment the equipment required
     * @return a list of exercises using the specified equipment
     */
    List<Exercise> findByEquipment(String equipment);

    /**
     * Finds exercises by difficulty level.
     *
     * @param difficultyLevel the difficulty level (e.g., 1 = easy, 5 = hard)
     * @return a list of exercises matching the difficulty level
     */
    List<Exercise> findByDifficultyLevel(int difficultyLevel);
}
