package com.example.oopproject.controllers;

import com.example.oopproject.models.Exercise;
import com.example.oopproject.repositories.ExerciseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // Activate the test profile
public class ExerciseControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ExerciseRepository exerciseRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    private String getBaseUrl() {
        return "http://localhost:" + port + "/exercises";
    }

    @BeforeEach
    public void setUp() {
        exerciseRepository.deleteAll();
    }

    @Test
    public void testCreateAndGetExercise() {
        Exercise exercise = new Exercise("Push Up", 10, 3, "workout1");
        exercise.setEquipment("None");
        exercise.setWeight(0);
        exercise.setMuscleGroups(List.of("Chest", "Arms"));

        ResponseEntity<Exercise> postResponse = restTemplate.postForEntity(getBaseUrl(), exercise, Exercise.class);
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Exercise saved = postResponse.getBody();
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Push Up");

        ResponseEntity<Exercise> getResponse = restTemplate.getForEntity(getBaseUrl() + "/" + saved.getId(), Exercise.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getName()).isEqualTo("Push Up");
    }

    @Test
    public void testUpdateExercise() {
        Exercise exercise = new Exercise("Squat", 15, 4, "workout2");
        Exercise saved = exerciseRepository.save(exercise);

        saved.setReps(20);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Exercise> requestEntity = new HttpEntity<>(saved, headers);

        ResponseEntity<Exercise> response = restTemplate.exchange(
                getBaseUrl() + "/" + saved.getId(),
                HttpMethod.PUT,
                requestEntity,
                Exercise.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getReps()).isEqualTo(20);
    }

    @Test
    public void testDeleteExercise() {
        Exercise exercise = new Exercise("Burpees", 12, 3, "workout3");
        Exercise saved = exerciseRepository.save(exercise);

        restTemplate.delete(getBaseUrl() + "/" + saved.getId());

        Optional<Exercise> deleted = exerciseRepository.findById(saved.getId());
        assertThat(deleted).isEmpty();
    }

    @Test
    public void testFilterExercise() {
        Exercise e1 = new Exercise("Sit Ups", 20, 4, "w1");
        e1.setEquipment("None");
        Exercise e2 = new Exercise("Sit Ups", 15, 3, "w2");
        e2.setEquipment("Mat");
        exerciseRepository.saveAll(List.of(e1, e2));

        String url = getBaseUrl() + "/filter?name=sit&reps=20";

        ResponseEntity<Exercise[]> response = restTemplate.getForEntity(url, Exercise[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).length).isEqualTo(1);
        assertThat(response.getBody()[0].getReps()).isEqualTo(20);
    }

    @Test
    public void testGetAllExercises() {
        exerciseRepository.save(new Exercise("Jumping Jacks", 30, 2, "w4"));
        exerciseRepository.save(new Exercise("Lunges", 10, 3, "w5"));

        ResponseEntity<Exercise[]> response = restTemplate.getForEntity(getBaseUrl(), Exercise[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).length).isEqualTo(2);
    }
}