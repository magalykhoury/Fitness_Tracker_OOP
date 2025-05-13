package com.example.oopproject.repositories;

import com.example.oopproject.models.Exercise;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class ExerciseRepositoryTest {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Test
    public void testCreateAndFindById() {
        Exercise exercise = new Exercise("Push Up", 15, 3, "workout123");
        Exercise saved = exerciseRepository.save(exercise);

        Optional<Exercise> found = exerciseRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Push Up", found.get().getName());
    }

    @Test
    public void testFindAll() {
        Exercise e1 = new Exercise("Squats", 20, 4, "workout1");
        Exercise e2 = new Exercise("Plank", 1, 3, "workout2");
        exerciseRepository.save(e1);
        exerciseRepository.save(e2);

        List<Exercise> all = exerciseRepository.findAll();
        assertTrue(all.size() >= 2);
    }

    @Test
    public void testDelete() {
        Exercise e = new Exercise("Lunges", 10, 2, "push up workout");
        Exercise saved = exerciseRepository.save(e);

        exerciseRepository.deleteById(saved.getId());
        assertFalse(exerciseRepository.existsById(saved.getId()));
    }
}
