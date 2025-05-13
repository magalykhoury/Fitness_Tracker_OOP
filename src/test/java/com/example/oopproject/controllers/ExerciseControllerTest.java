package com.example.oopproject.controllers;

import com.example.oopproject.models.Exercise;
import com.example.oopproject.repositories.ExerciseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.BDDMockito.*;

@WebMvcTest(ExerciseController.class)
public class ExerciseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllExercises() throws Exception {
        List<Exercise> exercises = List.of(
                new Exercise("1", "Jumping Jacks", 30, 2, "w1"),
                new Exercise("2", "Sit Ups", 20, 3, "w2")
        );

        given(exerciseRepository.findAll()).willReturn(exercises);

        mockMvc.perform(get("/exercises"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].name", is("Jumping Jacks")));
    }

    @Test
    public void testGetExerciseById_found() throws Exception {
        Exercise exercise = new Exercise("abc123", "Plank", 1, 4, "w99");
        given(exerciseRepository.findById("abc123")).willReturn(Optional.of(exercise));

        mockMvc.perform(get("/exercises/abc123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Plank")));
    }

    @Test
    public void testGetExerciseById_notFound() throws Exception {
        given(exerciseRepository.findById("missing")).willReturn(Optional.empty());

        mockMvc.perform(get("/exercises/missing"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateExercise() throws Exception {
        // Create the Exercise object with all necessary fields
        Exercise exercise = new Exercise("Crunches", 25, 2, "w3");
        // Create the saved Exercise object with an ID to simulate database save
        Exercise saved = new Exercise("xyz123", "Crunches", 25, 2, "w3");

        // Mock the repository's save method to return the saved object with the generated ID
        given(exerciseRepository.save(Mockito.any(Exercise.class))).willReturn(saved);

        // Perform the POST request to the "/exercises" endpoint
        mockMvc.perform(post("/exercises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exercise)))
                .andExpect(status().isOk())  // Expect an OK response
                .andExpect(jsonPath("$.id", is("xyz123")))  // Expect the ID to be returned
                .andExpect(jsonPath("$.name", is("Crunches")));  // Expect the name to be "Crunches"
    }


    @Test
    public void testDeleteExercise_found() throws Exception {
        given(exerciseRepository.existsById("del1")).willReturn(true);
        willDoNothing().given(exerciseRepository).deleteById("del1");

        mockMvc.perform(delete("/exercises/del1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteExercise_notFound() throws Exception {
        given(exerciseRepository.existsById("delX")).willReturn(false);

        mockMvc.perform(delete("/exercises/delX"))
                .andExpect(status().isNotFound());
    }
}
