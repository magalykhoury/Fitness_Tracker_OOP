package com.example.oopproject.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "exercises")
public class Exercise {

    @Id
    private String id;

    private String name;
    private int reps;
    private int sets;
    private String workoutId;
    private double weight;
    private String equipment;
    private int difficultyLevel;
    private List<String> muscleGroups;


    public Exercise(String name, int reps, int sets, String workoutId) {
        this.name = name;
        this.reps = reps;
        this.sets = sets;
        this.workoutId = workoutId;
        this.weight = weight;
        this.equipment = equipment;
    }
    public Exercise(String id, String name, int reps, int sets, String workoutId) {
        this.id = id;
        this.name = name;
        this.reps = reps;
        this.sets = sets;
        this.workoutId = workoutId;
    }


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

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public String getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(String workoutId) {
        this.workoutId = workoutId;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }
    public List<String> getMuscleGroups() {
        return muscleGroups;
    }

    public void setMuscleGroups(List<String> muscleGroups) {
        this.muscleGroups = muscleGroups;
    }
}
