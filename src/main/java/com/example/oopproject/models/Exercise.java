package com.example.oopproject.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Document(collection = "exercises")
public class Exercise {

    @Id
    private String id;

    @NotBlank(message = "Exercise name is required")
    private String name;

    private String description;

    private List<String> muscleGroups;

    private String equipment;
    private String workoutId;

    @Min(value = 1, message = "Difficulty level must be at least 1")
    private int difficultyLevel;

    private int sets;

    private int reps;

    private double weight;

    public Exercise() {
    }

    public Exercise(String name, String description, List<String> muscleGroups,
                    String equipment, int difficultyLevel, int sets, int reps, double weight) {
        this.name = name;
        this.description = description;
        this.muscleGroups = muscleGroups;
        this.equipment = equipment;
        this.difficultyLevel = difficultyLevel;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
    }

    // Getters and setters below...

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getMuscleGroups() {
        return muscleGroups;
    }

    public void setMuscleGroups(List<String> muscleGroups) {
        this.muscleGroups = muscleGroups;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    public String getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(String workoutId) {
        this.workoutId = workoutId;
    }
    @Override
    public String toString() {
        return "Exercise{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", muscleGroups=" + muscleGroups +
                ", equipment='" + equipment + '\'' +
                ", difficultyLevel=" + difficultyLevel +
                ", sets=" + sets +
                ", reps=" + reps +
                ", weight=" + weight +
                '}';
    }
}
