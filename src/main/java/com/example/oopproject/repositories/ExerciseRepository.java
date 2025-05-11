package com.example.oopproject.repositories;

import com.example.oopproject.models.Exercise;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends MongoRepository<Exercise, String> {
    List<Exercise> findByName(String name);
    List<Exercise> findByMuscleGroupsContaining(String muscleGroup);
    List<Exercise> findByEquipment(String equipment);
    List<Exercise> findByDifficultyLevel(int difficultyLevel);
}
