package com.example.oopproject.services;

import com.example.oopproject.exceptions.ResourceNotFoundException;
import com.example.oopproject.models.FitnessGoal;
import com.example.oopproject.repositories.FitnessGoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Service class for managing FitnessGoal entities.
 * Provides methods for CRUD operations and querying fitness goals by various criteria.
 */
@Service
public class FitnessGoalService {

    private final FitnessGoalRepository fitnessGoalRepository;

    /**
     * Constructor injecting FitnessGoalRepository.
     * @param fitnessGoalRepository repository for FitnessGoal persistence
     */
    @Autowired
    public FitnessGoalService(FitnessGoalRepository fitnessGoalRepository) {
        this.fitnessGoalRepository = fitnessGoalRepository;
    }

    /**
     * Retrieves all fitness goals.
     * @return list of all FitnessGoal entities
     */
    public List<FitnessGoal> getAllFitnessGoals() {
        return fitnessGoalRepository.findAll();
    }

    /**
     * Retrieves all fitness goals with pagination.
     * @param pageable pagination information
     * @return paginated list of FitnessGoal entities
     */
    public Page<FitnessGoal> getAllFitnessGoals(Pageable pageable) {
        return fitnessGoalRepository.findAll(pageable);
    }

    /**
     * Finds a fitness goal by its ID.
     * @param id the ID of the fitness goal
     * @return the FitnessGoal entity if found
     * @throws ResourceNotFoundException if fitness goal is not found
     */
    public FitnessGoal getFitnessGoalById(String id) {
        return fitnessGoalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fitness goal not found with id: " + id));
    }

    /**
     * Retrieves fitness goals by user ID.
     * @param userId the user ID
     * @return list of FitnessGoal entities for the user
     */
    public List<FitnessGoal> getFitnessGoalsByUserId(String userId) {
        return fitnessGoalRepository.findByUserId(userId);
    }

    /**
     * Retrieves fitness goals by user ID and goal type.
     * @param userId the user ID
     * @param goalType the goal type
     * @return list of FitnessGoal entities matching criteria
     */
    public List<FitnessGoal> getFitnessGoalsByUserIdAndType(String userId, String goalType) {
        return fitnessGoalRepository.findByUserIdAndGoalType(userId, goalType);
    }

    /**
     * Retrieves fitness goals by user ID and status.
     * @param userId the user ID
     * @param status the status
     * @return list of FitnessGoal entities matching criteria
     */
    public List<FitnessGoal> getFitnessGoalsByUserIdAndStatus(String userId, String status) {
        return fitnessGoalRepository.findByUserIdAndStatus(userId, status);
    }

    /**
     * Retrieves upcoming fitness goals before the specified date.
     * @param date the cutoff date
     * @return list of FitnessGoal entities with target dates before the given date
     */
    public List<FitnessGoal> getUpcomingFitnessGoals(Date date) {
        return fitnessGoalRepository.findByTargetDateBefore(date);
    }

    /**
     * Creates a new fitness goal.
     * @param fitnessGoal the FitnessGoal entity to create
     * @return the created FitnessGoal
     */
    public FitnessGoal createFitnessGoal(FitnessGoal fitnessGoal) {
        return fitnessGoalRepository.save(fitnessGoal);
    }

    /**
     * Updates a fitness goal identified by ID with new details.
     * If status is set to completed, sets the completed date.
     * Updates the updatedAt timestamp.
     * @param id the ID of the FitnessGoal to update
     * @param fitnessGoalDetails the updated FitnessGoal data
     * @return the updated FitnessGoal entity
     */
    public FitnessGoal updateFitnessGoal(String id, FitnessGoal fitnessGoalDetails) {
        FitnessGoal fitnessGoal = getFitnessGoalById(id);

        fitnessGoal.setUserId(fitnessGoalDetails.getUserId());
        fitnessGoal.setGoalType(fitnessGoalDetails.getGoalType());
        fitnessGoal.setDescription(fitnessGoalDetails.getDescription());
        fitnessGoal.setStartValue(fitnessGoalDetails.getStartValue());
        fitnessGoal.setTargetValue(fitnessGoalDetails.getTargetValue());
        fitnessGoal.setCurrentValue(fitnessGoalDetails.getCurrentValue());
        fitnessGoal.setTargetDate(fitnessGoalDetails.getTargetDate());
        fitnessGoal.setStartDate(fitnessGoalDetails.getStartDate());
        fitnessGoal.setStatus(fitnessGoalDetails.getStatus());

        // If status is changed to completed, set the completed date
        if ("completed".equals(fitnessGoalDetails.getStatus()) && fitnessGoal.getCompletedDate() == null) {
            fitnessGoal.setCompletedDate(new Date());
        }

        fitnessGoal.setUpdatedAt(new Date());

        return fitnessGoalRepository.save(fitnessGoal);
    }

    /**
     * Updates the progress of a fitness goal by setting the current value and optionally the status.
     * Sets completed date if status is changed to completed.
     * Updates the updatedAt timestamp.
     * @param id the ID of the FitnessGoal
     * @param currentValue the current progress value
     * @param status the status (optional)
     * @return the updated FitnessGoal entity
     */
    public FitnessGoal updateGoalProgress(String id, double currentValue, String status) {
        FitnessGoal fitnessGoal = getFitnessGoalById(id);

        fitnessGoal.setCurrentValue(currentValue);

        if (status != null && !status.isEmpty()) {
            fitnessGoal.setStatus(status);

            // If status is changed to completed, set the completed date
            if ("completed".equals(status) && fitnessGoal.getCompletedDate() == null) {
                fitnessGoal.setCompletedDate(new Date());
            }
        }

        fitnessGoal.setUpdatedAt(new Date());

        return fitnessGoalRepository.save(fitnessGoal);
    }

    /**
     * Deletes a fitness goal by ID.
     * @param id the ID of the FitnessGoal to delete
     */
    public void deleteFitnessGoal(String id) {
        FitnessGoal fitnessGoal = getFitnessGoalById(id);
        fitnessGoalRepository.delete(fitnessGoal);
    }
}
