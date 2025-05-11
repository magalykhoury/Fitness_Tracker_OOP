package com.example.oopproject.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

@Document(collection = "fitness_goals")
public class FitnessGoal {

    @Id
    private String id;

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Goal type is required")
    private String goalType; // e.g., "weight", "endurance", "strength"

    @NotBlank(message = "Goal description is required")
    private String description;

    private double startValue; // e.g., starting weight for weight loss goal
    private double targetValue; // e.g., target weight for weight loss goal
    private double currentValue; // e.g., current weight for weight loss goal

    @NotNull(message = "Target date is required")
    private Date targetDate;

    private Date startDate;
    private String status; // e.g., "in progress", "completed", "abandoned"
    private Date completedDate;
    private Date createdAt;
    private Date updatedAt;

    // Default constructor
    public FitnessGoal() {
        this.startDate = new Date();
        this.status = "in progress";
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Constructor with required fields
    public FitnessGoal(String userId, String goalType, String description,
                       double startValue, double targetValue, Date targetDate) {
        this.userId = userId;
        this.goalType = goalType;
        this.description = description;
        this.startValue = startValue;
        this.targetValue = targetValue;
        this.currentValue = startValue;
        this.targetDate = targetDate;
        this.startDate = new Date();
        this.status = "in progress";
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGoalType() {
        return goalType;
    }

    public void setGoalType(String goalType) {
        this.goalType = goalType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getStartValue() {
        return startValue;
    }

    public void setStartValue(double startValue) {
        this.startValue = startValue;
    }

    public double getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(double targetValue) {
        this.targetValue = targetValue;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public Date getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "FitnessGoal{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", goalType='" + goalType + '\'' +
                ", description='" + description + '\'' +
                ", startValue=" + startValue +
                ", targetValue=" + targetValue +
                ", currentValue=" + currentValue +
                ", targetDate=" + targetDate +
                ", startDate=" + startDate +
                ", status='" + status + '\'' +
                ", completedDate=" + completedDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}