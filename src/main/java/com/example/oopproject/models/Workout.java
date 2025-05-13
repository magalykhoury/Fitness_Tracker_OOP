package com.example.oopproject.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;
import org.bson.types.ObjectId;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Document(collection = "workouts")
public class Workout {

    @Id
    private ObjectId id;  // Changed from String to ObjectId

    private String name;

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotNull(message = "Date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date date;

    @Min(value = 1, message = "Duration must be at least 1 minute")
    private int duration; // in minutes

    @NotBlank(message = "Workout type is required")
    private String workoutType; // e.g., "cardio", "strength", "flexibility"

    private int caloriesBurned;

    private List<Exercise> exercises;

    public Workout() {
    }

    public Workout(String userId, Date date, int duration, String workoutType,
                   int caloriesBurned, List<Exercise> exercises) {
        this.userId = userId;
        this.date = date;
        this.duration = duration;
        this.workoutType = workoutType;
        this.caloriesBurned = caloriesBurned;
        this.exercises = exercises;
    }
    //lal testing
    public Workout(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

    // Getters and Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    // Convenience method to get ID as String
    public String getIdAsString() {
        return id != null ? id.toString() : null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    @Override
    public String toString() {
        return "Workout{" +
                "id='" + (id != null ? id.toString() : null) + '\'' +
                ", userId='" + userId + '\'' +
                ", date=" + date +
                ", duration=" + duration +
                ", workoutType='" + workoutType + '\'' +
                ", caloriesBurned=" + caloriesBurned +
                ", exercises=" + exercises +
                '}';
    }
}