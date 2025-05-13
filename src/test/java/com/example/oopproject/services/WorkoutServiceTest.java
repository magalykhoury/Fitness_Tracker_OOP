package com.example.oopproject.services;

import com.example.oopproject.exceptions.ResourceNotFoundException;
import com.example.oopproject.models.Workout;
import com.example.oopproject.repositories.WorkoutRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkoutServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @InjectMocks
    private WorkoutService workoutService;

    private AutoCloseable closeable;

    private Workout workout1;
    private Workout workout2;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        workout1 = new Workout("TestUser1", new Date(), 30, "cardio", 200, new ArrayList<>());
        workout1.setId(new ObjectId("656f2d70fc13ae5b80000001"));

        workout2 = new Workout("TestUser2", new Date(), 45, "strength", 350, new ArrayList<>());
        workout2.setId(new ObjectId("656f2d70fc13ae5b80000002"));
    }

    @Test
    void testGetAllWorkouts() {
        List<Workout> mockList = Arrays.asList(workout1, workout2);
        when(workoutRepository.findAll()).thenReturn(mockList);

        List<Workout> result = workoutService.getAllWorkouts();

        assertEquals(2, result.size());
        verify(workoutRepository, times(1)).findAll();
    }

    @Test
    void testGetWorkoutById_ValidId() {
        String id = workout1.getId().toHexString();
        when(workoutRepository.findById(id)).thenReturn(Optional.of(workout1));

        Workout result = workoutService.getWorkoutById(id);

        assertNotNull(result);
        assertEquals("cardio", result.getWorkoutType());
        verify(workoutRepository).findById(id);
    }

    @Test
    void testGetWorkoutById_InvalidId() {
        String invalidId = "nonexistent-id";
        when(workoutRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            workoutService.getWorkoutById(invalidId);
        });
    }

    @Test
    void testCreateWorkout() {
        when(workoutRepository.save(any(Workout.class))).thenReturn(workout1);

        Workout created = workoutService.createWorkout(workout1);

        assertEquals("cardio", created.getWorkoutType());
        verify(workoutRepository).save(workout1);
    }

    @Test
    void testDeleteWorkout() {
        String id = workout1.getId().toHexString();
        when(workoutRepository.findById(id)).thenReturn(Optional.of(workout1));

        workoutService.deleteWorkout(id);

        verify(workoutRepository).delete(workout1);
    }

    @Test
    void testUpdateWorkout() {
        String id = workout1.getId().toHexString();
        Workout updated = new Workout("UpdatedUser", new Date(), 60, "yoga", 150, new ArrayList<>());

        when(workoutRepository.findById(id)).thenReturn(Optional.of(workout1));
        when(workoutRepository.save(any(Workout.class))).thenReturn(updated);

        Workout result = workoutService.updateWorkout(id, updated);

        assertEquals("yoga", result.getWorkoutType());
        assertEquals(60, result.getDuration());
    }

    @Test
    void testSearchWorkouts_ByTypeAndUserId() {
        when(workoutRepository.findByUserIdAndWorkoutType("TestUser1", "cardio"))
                .thenReturn(List.of(workout1));

        List<Workout> result = workoutService.searchWorkouts("cardio", "TestUser1");

        assertEquals(1, result.size());
        assertEquals("cardio", result.get(0).getWorkoutType());
    }

    @Test
    void testPaginatedWorkouts() {
        PageRequest pageable = PageRequest.of(0, 2);
        Page<Workout> mockPage = new PageImpl<>(List.of(workout1, workout2));

        when(workoutRepository.findAll(pageable)).thenReturn(mockPage);

        Page<Workout> result = workoutService.getAllWorkouts(pageable);

        assertEquals(2, result.getContent().size());
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
