package com.example.oopproject.repositories;

import com.example.oopproject.models.Workout;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class WorkoutRepositoryTest {

    @Autowired
    private WorkoutRepository workoutRepository;

    @Test
    void testSaveAndFindWorkout() {
        Workout workout = new Workout("Yoga", 45);
        Workout saved = workoutRepository.save(workout);

        Optional<Workout> found = workoutRepository.findById(saved.getId().toString());


        assertTrue(found.isPresent());
        assertEquals("Yoga", found.get().getName());
    }
}
