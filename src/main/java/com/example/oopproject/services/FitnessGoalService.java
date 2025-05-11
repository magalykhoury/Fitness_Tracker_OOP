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

@Service
public class FitnessGoalService {

    private final FitnessGoalRepository fitnessGoalRepository;

    @Autowired
    public FitnessGoalService(FitnessGoalRepository fitnessGoalRepository) {
        this.fitnessGoalRepository = fitnessGoalRepository;
    }

    public List<FitnessGoal> getAllFitnessGoals() {
        return fitnessGoalRepository.findAll();
    }

    public Page<FitnessGoal> getAllFitnessGoals(Pageable pageable) {
        return fitnessGoalRepository.findAll(pageable);
    }

    public FitnessGoal getFitnessGoalById(String id) {
        return fitnessGoalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fitness goal not found with id: " + id));
    }

    public List<FitnessGoal> getFitnessGoalsByUserId(String userId) {
        return fitnessGoalRepository.findByUserId(userId);
    }

    public List<FitnessGoal> getFitnessGoalsByUserIdAndType(String userId, String goalType) {
        return fitnessGoalRepository.findByUserIdAndGoalType(userId, goalType);
    }

    public List<FitnessGoal> getFitnessGoalsByUserIdAndStatus(String userId, String status) {
        return fitnessGoalRepository.findByUserIdAndStatus(userId, status);
    }

    public List<FitnessGoal> getUpcomingFitnessGoals(Date date) {
        return fitnessGoalRepository.findByTargetDateBefore(date);
    }

    public FitnessGoal createFitnessGoal(FitnessGoal fitnessGoal) {
        return fitnessGoalRepository.save(fitnessGoal);
    }

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

    public void deleteFitnessGoal(String id) {
        FitnessGoal fitnessGoal = getFitnessGoalById(id);
        fitnessGoalRepository.delete(fitnessGoal);
    }
}