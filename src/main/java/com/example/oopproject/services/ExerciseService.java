package com.example.oopproject.services;

import com.example.oopproject.models.Exercise;
import com.example.oopproject.repositories.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing Exercise entities.
 * Provides CRUD operations and filtering/pagination capabilities.
 */
@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    /**
     * Constructor for ExerciseService with dependency injection of ExerciseRepository.
     * @param exerciseRepository the repository to manage Exercise data access
     */
    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    /**
     * Creates a new Exercise.
     * @param exercise the Exercise entity to create
     * @return the created Exercise
     */
    public Exercise createExercise(Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    /**
     * Retrieves all exercises.
     * @return list of all Exercise entities
     */
    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    /**
     * Updates an existing Exercise by ID or creates it if not found.
     * @param id the ID of the Exercise to update
     * @param updatedExercise the Exercise data to update
     * @return the updated or newly created Exercise
     */
    public Exercise updateExercise(String id, Exercise updatedExercise) {
        return exerciseRepository.findById(id)
                .map(exercise -> {
                    exercise.setName(updatedExercise.getName());
                    exercise.setReps(updatedExercise.getReps());
                    exercise.setSets(updatedExercise.getSets());
                    exercise.setWorkoutId(updatedExercise.getWorkoutId());
                    // Added from first code: weight, equipment, muscleGroups
                    exercise.setWeight(updatedExercise.getWeight());
                    exercise.setEquipment(updatedExercise.getEquipment());
                    exercise.setMuscleGroups(updatedExercise.getMuscleGroups());
                    return exerciseRepository.save(exercise);
                })
                .orElseGet(() -> {
                    updatedExercise.setId(id);
                    return exerciseRepository.save(updatedExercise);
                });
    }

    /**
     * Deletes an Exercise by ID if it exists.
     * @param id the ID of the Exercise to delete
     */
    public void deleteExercise(String id) {
        if (exerciseRepository.existsById(id)) {
            exerciseRepository.deleteById(id);
        }
    }

    /**
     * Finds an Exercise by its ID.
     * @param id the ID of the Exercise
     * @return the Exercise if found, or null if not found
     */
    public Exercise getExerciseById(String id) {
        return exerciseRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves a paginated list of exercises sorted by a given field and direction.
     * @param page the page number (zero-based)
     * @param size the size of the page
     * @param sortBy the field to sort by
     * @param direction the sort direction ("asc" or "desc")
     * @return a Page of Exercise entities matching the criteria
     */
    public Page<Exercise> getExercisesPaginated(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return exerciseRepository.findAll(pageable);
    }

    /**
     * Filters exercises by reps, sets, name, and workoutId criteria.
     * Null parameters mean no filtering on that field.
     * @param reps the number of reps to filter by, or null
     * @param sets the number of sets to filter by, or null
     * @param name substring to filter by exercise name, or null
     * @param workoutId the workout ID to filter by, or null
     * @return list of Exercise entities matching all provided filters
     */
    public List<Exercise> getExercisesByFilter(Integer reps, Integer sets, String name, String workoutId) {
        List<Exercise> exercises = exerciseRepository.findAll();

        return exercises.stream()
                .filter(exercise -> reps == null || exercise.getReps() == reps)
                .filter(exercise -> sets == null || exercise.getSets() == sets)
                .filter(exercise -> name == null || exercise.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(exercise -> workoutId == null || exercise.getWorkoutId().equals(workoutId))
                .collect(Collectors.toList());
    }
}

