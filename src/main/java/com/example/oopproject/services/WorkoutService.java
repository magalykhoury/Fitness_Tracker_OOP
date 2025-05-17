package com.example.oopproject.services;

import com.example.oopproject.models.Workout;
import com.example.oopproject.repositories.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing Workout entities.
 * Provides methods to create, read, update, delete,
 * paginate, filter by date, and search workouts.
 */
@Service
public class WorkoutService {

    @Autowired
    private WorkoutRepository workoutRepository;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Creates and saves a new Workout.
     * @param workout the Workout entity to create
     * @return the saved Workout entity
     */
    public Workout createWorkout(Workout workout) {
        return workoutRepository.save(workout);
    }

    /**
     * Retrieves all workouts.
     * @return list of all Workout entities
     */
    public List<Workout> getAllWorkouts() {
        return workoutRepository.findAll();
    }

    /**
     * Finds a workout by its ID.
     * @param id the workout ID
     * @return the Workout entity if found, otherwise null
     */
    public Workout getWorkoutById(String id) {
        Optional<Workout> workout = workoutRepository.findById(id);
        return workout.orElse(null);
    }

    /**
     * Updates an existing workout by ID.
     * If workout exists, updates its fields and saves it.
     * @param id the workout ID to update
     * @param updatedWorkout Workout object with updated data
     * @return the updated Workout entity, or null if not found
     */
    public Workout updateWorkout(String id, Workout updatedWorkout) {
        Optional<Workout> workout = workoutRepository.findById(id);
        if (workout.isPresent()) {
            Workout existingWorkout = workout.get();
            existingWorkout.setWorkoutType(updatedWorkout.getWorkoutType());
            existingWorkout.setDuration(updatedWorkout.getDuration());
            existingWorkout.setCaloriesBurned(updatedWorkout.getCaloriesBurned());
            existingWorkout.setDate(updatedWorkout.getDate());
            return workoutRepository.save(existingWorkout);
        }
        return null;
    }

    /**
     * Deletes a workout by its ID.
     * @param id the workout ID to delete
     */
    public void deleteWorkout(String id) {
        workoutRepository.deleteById(id);
    }

    /**
     * Retrieves a paginated and sorted list of workouts.
     * @param page the page number (0-based)
     * @param size number of items per page
     * @param sortBy field to sort by
     * @param sortDir direction of sorting ("asc" or "desc")
     * @return list of workouts for the requested page and sort order
     */
    public List<Workout> getPaginatedWorkouts(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Workout> workoutPage = workoutRepository.findAll(pageable);

        return workoutPage.getContent();
    }

    /**
     * Filters workouts by a date range.
     * @param startDateStr start date in "yyyy-MM-dd" format
     * @param endDateStr end date in "yyyy-MM-dd" format
     * @return list of workouts whose date is between startDate and endDate
     * @throws ParseException if date parsing fails
     */
    public List<Workout> filterByDateRange(String startDateStr, String endDateStr) throws ParseException {
        Date startDate = dateFormat.parse(startDateStr);
        Date endDate = dateFormat.parse(endDateStr);

        return workoutRepository.findByDateBetween(startDate, endDate);
    }

    /**
     * Searches workouts by workout type and optionally filters by user ID.
     * @param workoutType the workout type to search for
     * @param userId optional user ID to filter workouts by user
     * @return list of workouts matching the criteria
     */
    public List<Workout> searchWorkouts(String workoutType, String userId) {
        if (userId != null && !userId.isEmpty()) {
            return workoutRepository.findByUserIdAndWorkoutType(userId, workoutType);
        } else {
            return workoutRepository.findByWorkoutType(workoutType);
        }
    }
}
