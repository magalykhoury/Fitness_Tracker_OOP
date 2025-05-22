package com.example.oopproject.controllers;

import com.example.oopproject.models.Workout;
import com.example.oopproject.repositories.WorkoutRepository;
import com.example.oopproject.services.WorkoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class WorkoutControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private WorkoutService workoutService;

    @Autowired
    private WebApplicationContext context;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    void setup() {
        // Configure MockMvc with security
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        workoutRepository.deleteAll();

        // Create test workout 1
        Workout workout1 = new Workout();
        workout1.setName("Pushups");
        workout1.setWorkoutType("strength");
        workout1.setDuration(10);
        workout1.setCaloriesBurned(100);
        workout1.setDate(new Date());
        workoutRepository.save(workout1);

        // Create test workout 2
        Workout workout2 = new Workout();
        workout2.setName("Running");
        workout2.setWorkoutType("cardio");
        workout2.setDuration(20);
        workout2.setCaloriesBurned(200);
        workout2.setDate(new Date());
        workoutRepository.save(workout2);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetAllWorkouts() throws Exception {
        mockMvc.perform(get("/api/workouts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCreateWorkout() throws Exception {
        String workoutJson = """
                {
                    "name": "Cycling",
                    "workoutType": "cardio",
                    "duration": 45,
                    "caloriesBurned": 350,
                    "date": "2025-05-17"
                }
                """;

        mockMvc.perform(post("/api/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(workoutJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cycling"))
                .andExpect(jsonPath("$.workoutType").value("cardio"))
                .andExpect(jsonPath("$.duration").value(45))
                .andExpect(jsonPath("$.caloriesBurned").value(350));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetWorkoutById() throws Exception {
        // First get all workouts to get an ID
        String responseJson = mockMvc.perform(get("/api/workouts"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract the ID from the response (simplified approach)
        String id = responseJson.split("\"id\":\"")[1].split("\"")[0];

        // Now test getting by ID
        mockMvc.perform(get("/api/workouts/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUpdateWorkout() throws Exception {
        // First create a workout
        String createJson = """
                {
                    "name": "Yoga",
                    "workoutType": "flexibility",
                    "duration": 30,
                    "caloriesBurned": 150,
                    "date": "2025-05-17"
                }
                """;

        String createResponse = mockMvc.perform(post("/api/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String id = createResponse.split("\"id\":\"")[1].split("\"")[0];

        // Now update it
        String updateJson = """
    {
        "name": "Advanced Yoga",
        "workoutType": "flexibility",
        "duration": 45,
        "caloriesBurned": 200,
        "date": "2025-05-17"
    }
    """;


        mockMvc.perform(put("/api/workouts/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Advanced Yoga"))
                .andExpect(jsonPath("$.duration").value(45))
                .andExpect(jsonPath("$.caloriesBurned").value(200));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeleteWorkout() throws Exception {
        // First create a workout
        String createJson = """
                {
                    "name": "Swimming",
                    "workoutType": "cardio",
                    "duration": 40,
                    "caloriesBurned": 400,
                    "date": "2025-05-17"
                }
                """;

        String createResponse = mockMvc.perform(post("/api/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String id = createResponse.split("\"id\":\"")[1].split("\"")[0];

        // Now delete it
        mockMvc.perform(delete("/api/workouts/" + id))
                .andExpect(status().isNoContent());

        // Verify it's gone
        mockMvc.perform(get("/api/workouts/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetPaginatedWorkouts() throws Exception {
        // Delete any existing workouts to ensure we start clean
        workoutRepository.deleteAll();

        // Create workouts with specific durations to test pagination order
        for (int i = 0; i < 5; i++) {
            Workout workout = new Workout();
            workout.setName("Workout " + i);
            workout.setWorkoutType("test");
            workout.setDuration(10 + i); // This creates workouts with durations 10, 11, 12, 13, 14
            workout.setCaloriesBurned(100 + i * 10);
            workout.setDate(new Date());
            workoutRepository.save(workout);
        }


        mockMvc.perform(get("/api/workouts/paginated")
                        .param("page", "0")
                        .param("size", "3")
                        .param("sortBy", "duration")
                        .param("sortDir", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3))) // Should return 3 results per page
                .andExpect(jsonPath("$[0].duration").value(14)); // First result should be duration 14 (highest)
    }

    @Test
    @WithMockUser(roles = "USER")
    void testFilterByDateRange() throws Exception {
        // Create workouts with specific dates
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Workout pastWorkout = new Workout();
        pastWorkout.setName("Past Workout");
        pastWorkout.setWorkoutType("test");
        pastWorkout.setDuration(30);
        pastWorkout.setDate(formatter.parse("2025-04-01"));
        workoutRepository.save(pastWorkout);

        Workout currentWorkout = new Workout();
        currentWorkout.setName("Current Workout");
        currentWorkout.setWorkoutType("test");
        currentWorkout.setDuration(30);
        currentWorkout.setDate(formatter.parse("2025-05-15"));
        workoutRepository.save(currentWorkout);


        mockMvc.perform(get("/api/workouts/filterByDate")
                        .param("startDate", "2025-05-01")
                        .param("endDate", "2025-05-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.name == 'Current Workout')]").exists())
                .andExpect(jsonPath("$[?(@.name == 'Past Workout')]").doesNotExist());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testSearchWorkouts() throws Exception {

        Workout specialWorkout = new Workout();
        specialWorkout.setName("Special Workout");
        specialWorkout.setWorkoutType("hiit");
        specialWorkout.setDuration(20);
        specialWorkout.setUserId("user123");
        specialWorkout.setDate(new Date());
        workoutRepository.save(specialWorkout);


        mockMvc.perform(get("/api/workouts/search")
                        .param("workoutType", "hiit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Special Workout"));


        mockMvc.perform(get("/api/workouts/search")
                        .param("workoutType", "hiit")
                        .param("userId", "user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userId").value("user123"));


        mockMvc.perform(get("/api/workouts/search")
                        .param("workoutType", "hiit")
                        .param("userId", "nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}