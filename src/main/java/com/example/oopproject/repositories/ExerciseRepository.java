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

    /**
     * Finds exercises by name (case-insensitive containing match).
     *
     * @param name the name to search for
     * @return a list of exercises with names containing the specified string
     */
    List<Exercise> findByNameContainingIgnoreCase(String name);

    /**
     * Finds exercises by the number of repetitions.
     *
     * @param reps the number of repetitions to search for
     * @return a list of exercises with the specified number of repetitions
     */
    List<Exercise> findByReps(int reps);

    /**
     * Finds exercises by the number of sets.
     *
     * @param sets the number of sets to search for
     * @return a list of exercises with the specified number of sets
     */
    List<Exercise> findBySets(int sets);

    /**
     * Finds exercises associated with a specific workout.
     *
     * @param workoutId the ID of the workout to search for
     * @return a list of exercises associated with the specified workout
     */
    List<Exercise> findByWorkoutId(String workoutId);

    /**
     * Finds exercises by name and repetitions.
     *
     * @param name the name to search for
     * @param reps the number of repetitions to search for
     * @return a list of exercises matching both criteria
     */
    List<Exercise> findByNameContainingIgnoreCaseAndReps(String name, int reps);

    /**
     * Finds exercises by name and sets.
     *
     * @param name the name to search for
     * @param sets the number of sets to search for
     * @return a list of exercises matching both criteria
     */
    List<Exercise> findByNameContainingIgnoreCaseAndSets(String name, int sets);

    /**
     * Finds exercises by repetitions and sets.
     *
     * @param reps the number of repetitions to search for
     * @param sets the number of sets to search for
     * @return a list of exercises matching both criteria
     */
    List<Exercise> findByRepsAndSets(int reps, int sets);
}
