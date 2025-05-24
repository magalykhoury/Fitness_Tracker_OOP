package com.example.oopproject.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.bson.types.ObjectId;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Represents a workout session for a user.
 */
@Document(collection = "workouts")
public class Workout {

    /**
     * Unique identifier for the workout.
     */
    @Id
    private String id;

    /**
     * Name of the workout.
     */
    private String name;

    /**
     * ID of the user who performed the workout.
     */
    @NotBlank(message = "User ID is required")
    private String userId;

    /**
     * Date and time when the workout took place.
     */
    @NotNull(message = "Date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date date;

    /**
     * Duration of the workout in minutes.
     */
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private int duration; // in minutes

    /**
     * Type of the workout, e.g., "cardio", "strength", "flexibility".
     */
    @NotBlank(message = "Workout type is required")
    private String workoutType;

    /**
     * Number of calories burned during the workout.
     */
    private int caloriesBurned;

    /**
     * List of exercises included in this workout.
     */
    private List<Exercise> exercises;

    /**
     * Default constructor.
     */
    public Workout() {
    }

    /**
     * Constructs a Workout with all required fields.
     *
     * @param userId ID of the user performing the workout
     * @param date Date and time of the workout
     * @param duration Duration in minutes
     * @param workoutType Type of workout
     * @param caloriesBurned Calories burned during workout
     * @param exercises List of exercises
     */
    public Workout(String userId, Date date, int duration, String workoutType,
                   int caloriesBurned, List<Exercise> exercises) {
        this.userId = userId;
        this.date = date;
        this.duration = duration;
        this.workoutType = workoutType;
        this.caloriesBurned = caloriesBurned;
        this.exercises = exercises;
    }

    /**
     * Convenience constructor with name and duration.
     *
     * @param name Name of the workout
     * @param duration Duration in minutes
     */
    public Workout(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

    /**
     * Gets the unique workout ID.
     *
     * @return the workout ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique workout ID.
     *
     * @param id the workout ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the workout ID as a string.
     *
     * @return the workout ID as a string, or null if ID is null
     */
    public String getIdAsString() {
        return id != null ? id.toString() : null;
    }

    /**
     * Gets the workout name.
     *
     * @return the workout name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the workout name.
     *
     * @param name the workout name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the user ID associated with this workout.
     *
     * @return the user ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user ID associated with this workout.
     *
     * @param userId the user ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets the date and time of the workout.
     *
     * @return the workout date and time
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date and time of the workout.
     *
     * @param date the workout date and time
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Gets the duration of the workout in minutes.
     *
     * @return the duration in minutes
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the workout in minutes.
     *
     * @param duration the duration in minutes
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Gets the type of the workout.
     *
     * @return the workout type
     */
    public String getWorkoutType() {
        return workoutType;
    }

    /**
     * Sets the type of the workout.
     *
     * @param workoutType the workout type
     */
    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    /**
     * Gets the number of calories burned during the workout.
     *
     * @return the calories burned
     */
    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    /**
     * Sets the number of calories burned during the workout.
     *
     * @param caloriesBurned the calories burned
     */
    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    /**
     * Gets the list of exercises included in the workout.
     *
     * @return the exercises list
     */
    public List<Exercise> getExercises() {
        return exercises;
    }

    /**
     * Sets the list of exercises included in the workout.
     *
     * @param exercises the exercises list
     */
    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    /**
     * Returns a string representation of the workout.
     *
     * @return workout details as a string
     */
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
