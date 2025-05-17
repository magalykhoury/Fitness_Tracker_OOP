package com.example.oopproject.services;

import com.example.oopproject.models.Workout;
import com.example.oopproject.repositories.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class WorkoutServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @InjectMocks
    private WorkoutService workoutService;

    private Workout workout1;
    private Workout workout2;
    private List<Workout> workoutList;
    private SimpleDateFormat dateFormat;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        workout1 = new Workout();
        workout1.setId("1");
        workout1.setName("Running");
        workout1.setWorkoutType("cardio");
        workout1.setDuration(30);
        workout1.setCaloriesBurned(300);
        try {
            workout1.setDate(dateFormat.parse("2025-05-15"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        workout2 = new Workout();
        workout2.setId("2");
        workout2.setName("Weight Training");
        workout2.setWorkoutType("strength");
        workout2.setDuration(45);
        workout2.setCaloriesBurned(200);
        try {
            workout2.setDate(dateFormat.parse("2025-05-16"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        workoutList = new ArrayList<>();
        workoutList.add(workout1);
        workoutList.add(workout2);
    }

    @Test
    void testCreateWorkout() {
        when(workoutRepository.save(any(Workout.class))).thenReturn(workout1);

        Workout result = workoutService.createWorkout(workout1);

        assertNotNull(result);
        assertEquals("Running", result.getName());
        assertEquals("cardio", result.getWorkoutType());
        verify(workoutRepository, times(1)).save(any(Workout.class));
    }

    @Test
    void testGetAllWorkouts() {
        when(workoutRepository.findAll()).thenReturn(workoutList);

        List<Workout> result = workoutService.getAllWorkouts();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(workoutRepository, times(1)).findAll();
    }

    @Test
    void testGetWorkoutById_Found() {
        when(workoutRepository.findById("1")).thenReturn(Optional.of(workout1));

        Workout result = workoutService.getWorkoutById("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Running", result.getName());
        verify(workoutRepository, times(1)).findById("1");
    }

    @Test
    void testGetWorkoutById_NotFound() {
        when(workoutRepository.findById("999")).thenReturn(Optional.empty());

        Workout result = workoutService.getWorkoutById("999");

        assertNull(result);
        verify(workoutRepository, times(1)).findById("999");
    }

    @Test
    void testUpdateWorkout_Success() {
        when(workoutRepository.findById("1")).thenReturn(Optional.of(workout1));
        when(workoutRepository.save(any(Workout.class))).thenReturn(workout1);

        Workout updatedWorkout = new Workout();
        updatedWorkout.setWorkoutType("hiit");
        updatedWorkout.setDuration(20);
        updatedWorkout.setCaloriesBurned(250);
        updatedWorkout.setDate(new Date());

        Workout result = workoutService.updateWorkout("1", updatedWorkout);

        assertNotNull(result);
        assertEquals("hiit", result.getWorkoutType());
        assertEquals(20, result.getDuration());
        assertEquals(250, result.getCaloriesBurned());
        verify(workoutRepository, times(1)).findById("1");
        verify(workoutRepository, times(1)).save(any(Workout.class));
    }

    @Test
    void testUpdateWorkout_NotFound() {
        when(workoutRepository.findById("999")).thenReturn(Optional.empty());

        Workout updatedWorkout = new Workout();
        updatedWorkout.setWorkoutType("hiit");

        Workout result = workoutService.updateWorkout("999", updatedWorkout);

        assertNull(result);
        verify(workoutRepository, times(1)).findById("999");
        verify(workoutRepository, never()).save(any(Workout.class));
    }

    @Test
    void testDeleteWorkout() {
        doNothing().when(workoutRepository).deleteById("1");

        workoutService.deleteWorkout("1");

        verify(workoutRepository, times(1)).deleteById("1");
    }

    @Test
    void testGetPaginatedWorkouts() {
        Page<Workout> page = new PageImpl<>(workoutList);
        when(workoutRepository.findAll(any(Pageable.class))).thenReturn(page);

        List<Workout> result = workoutService.getPaginatedWorkouts(0, 10, "date", "asc");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(workoutRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testFilterByDateRange() throws ParseException {
        Date startDate = dateFormat.parse("2025-05-15");
        Date endDate = dateFormat.parse("2025-05-20");

        when(workoutRepository.findByDateBetween(startDate, endDate)).thenReturn(workoutList);

        List<Workout> result = workoutService.filterByDateRange("2025-05-15", "2025-05-20");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(workoutRepository, times(1)).findByDateBetween(any(Date.class), any(Date.class));
    }

    @Test
    void testSearchWorkouts_WithUserId() {
        List<Workout> userWorkouts = new ArrayList<>();
        userWorkouts.add(workout1);

        when(workoutRepository.findByUserIdAndWorkoutType("user123", "cardio")).thenReturn(userWorkouts);

        List<Workout> result = workoutService.searchWorkouts("cardio", "user123");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Running", result.get(0).getName());
        verify(workoutRepository, times(1)).findByUserIdAndWorkoutType("user123", "cardio");
    }

    @Test
    void testSearchWorkouts_WithoutUserId() {
        List<Workout> typeWorkouts = new ArrayList<>();
        typeWorkouts.add(workout1);

        when(workoutRepository.findByWorkoutType("cardio")).thenReturn(typeWorkouts);

        List<Workout> result = workoutService.searchWorkouts("cardio", null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Running", result.get(0).getName());
        verify(workoutRepository, times(1)).findByWorkoutType("cardio");
    }
}