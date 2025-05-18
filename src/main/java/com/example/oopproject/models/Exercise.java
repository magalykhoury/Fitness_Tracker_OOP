package com.example.oopproject.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an exercise entity stored in the MongoDB collection "exercises".
 */
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

    /**
     * Constructs an Exercise with the given name, reps, sets, and workout ID.
     *
     * @param name the name of the exercise
     * @param reps the number of repetitions
     * @param sets the number of sets
     * @param workoutId the ID of the associated workout
     */
    public Exercise(String name, int reps, int sets, String workoutId) {
        this.name = name;
        this.reps = reps;
        this.sets = sets;
        this.workoutId = workoutId;
        this.weight = 0.0;
        this.equipment = "";
        this.muscleGroups = new ArrayList<>();
    }

    /**
     * Constructs an Exercise with the given ID, name, reps, sets, and workout ID.
     *
     * @param id the unique identifier of the exercise
     * @param name the name of the exercise
     * @param reps the number of repetitions
     * @param sets the number of sets
     * @param workoutId the ID of the associated workout
     */
    public Exercise(String id, String name, int reps, int sets, String workoutId) {
        this.id = id;
        this.name = name;
        this.reps = reps;
        this.sets = sets;
        this.workoutId = workoutId;
        this.muscleGroups = new ArrayList<>();
        this.weight = 0.0;
        this.equipment = "";
    }

    /**
     * Default no-argument constructor.
     */
    public Exercise() {
        this.muscleGroups = new ArrayList<>();
        this.weight = 0.0;
        this.equipment = "";
    }

    /**
     * Gets the ID of the exercise.
     *
     * @return the exercise ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the exercise.
     *
     * @param id the exercise ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the name of the exercise.
     *
     * @return the exercise name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the exercise.
     *
     * @param name the exercise name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the number of repetitions.
     *
     * @return the number of reps
     */
    public int getReps() {
        return reps;
    }

    /**
     * Sets the number of repetitions.
     *
     * @param reps the number of reps
     */
    public void setReps(int reps) {
        this.reps = reps;
    }

    /**
     * Gets the number of sets.
     *
     * @return the number of sets
     */
    public int getSets() {
        return sets;
    }

    /**
     * Sets the number of sets.
     *
     * @param sets the number of sets
     */
    public void setSets(int sets) {
        this.sets = sets;
    }

    /**
     * Gets the associated workout ID.
     *
     * @return the workout ID
     */
    public String getWorkoutId() {
        return workoutId;
    }

    /**
     * Sets the associated workout ID.
     *
     * @param workoutId the workout ID
     */
    public void setWorkoutId(String workoutId) {
        this.workoutId = workoutId;
    }

    /**
     * Gets the weight used in the exercise.
     *
     * @return the weight
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Sets the weight used in the exercise.
     *
     * @param weight the weight
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Gets the equipment used for the exercise.
     *
     * @return the equipment
     */
    public String getEquipment() {
        return equipment;
    }

    /**
     * Sets the equipment used for the exercise.
     *
     * @param equipment the equipment
     */
    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    /**
     * Gets the muscle groups targeted by the exercise.
     *
     * @return a list of muscle group names
     */
    public List<String> getMuscleGroups() {
        return muscleGroups;
    }

    /**
     * Sets the muscle groups targeted by the exercise.
     *
     * @param muscleGroups a list of muscle group names
     */
    public void setMuscleGroups(List<String> muscleGroups) {
        this.muscleGroups = muscleGroups;
    }
}

