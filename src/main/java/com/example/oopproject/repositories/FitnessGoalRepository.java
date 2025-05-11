package com.example.oopproject.repositories;


import com.example.oopproject.models.FitnessGoal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FitnessGoalRepository extends MongoRepository<FitnessGoal, String> {
    List<FitnessGoal> findByUserId(String userId);
    List<FitnessGoal> findByUserIdAndGoalType(String userId, String goalType);
    List<FitnessGoal> findByUserIdAndStatus(String userId, String status);
    List<FitnessGoal> findByTargetDateBefore(Date date);
}