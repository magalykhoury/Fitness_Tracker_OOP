package com.example.oopproject.controllers;

import com.example.oopproject.models.Workout;
import com.example.oopproject.repositories.WorkoutRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WorkoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        workoutRepository.deleteAll();
    }

    @Test
    void testCreateWorkout() throws Exception {
        Workout workout = new Workout("Running", 20);

        mockMvc.perform(post("/api/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workout)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Running"))
                .andExpect(jsonPath("$.duration").value(20));
    }

    @Test
    void testGetAllWorkouts() throws Exception {
        workoutRepository.save(new Workout("Swimming", 40));

        mockMvc.perform(get("/api/workouts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Swimming"));
    }
}
