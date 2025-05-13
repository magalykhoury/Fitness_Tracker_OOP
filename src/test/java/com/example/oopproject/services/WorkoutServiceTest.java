package com.example.oopproject.services;

import com.example.oopproject.models.Workout;
import com.example.oopproject.repositories.WorkoutRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class WorkoutServiceTest {

    @Autowired
    private WorkoutService workoutService;

    @MockBean
    private WorkoutRepository workoutRepository;

    @Test
    void testCreateWorkout() {
        Workout sampleWorkout = new Workout("Cardio", 30);
        when(workoutRepository.save(any(Workout.class))).thenReturn(sampleWorkout);

        Workout result = workoutService.createWorkout(sampleWorkout);

        assertEquals("Cardio", result.getName());
        assertEquals(30, result.getDuration());
    }
}
