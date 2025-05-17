package com.example.oopproject.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Date;

/**
 * Represents a user of the fitness application.
 */
@Document(collection = "users")
public class User {

    /**
     * Role of the user (default is "user").
     */
    private String role = "user";

    /**
     * Unique identifier for the user.
     */
    @Id
    private String id;

    /**
     * Username of the user.
     */
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    /**
     * Email address of the user.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    /**
     * Password of the user.
     */
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    /**
     * First name of the user.
     */
    private String firstName;

    /**
     * Last name of the user.
     */
    private String lastName;

    /**
     * Date of birth of the user.
     */
    private Date dateOfBirth;

    /**
     * Height of the user in centimeters.
     */
    private double height;

    /**
     * Weight of the user in kilograms.
     */
    private double weight;

    /**
     * User's fitness goal (e.g., "weight loss", "muscle gain", "endurance").
     */
    private String fitnessGoal;

    /**
     * Timestamp of when the user was created.
     */
    private Date createdAt;

    /**
     * Timestamp of when the user was last updated.
     */
    private Date updatedAt;

    /**
     * Default constructor. Initializes creation and update timestamps.
     */
    public User() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    /**
     * Constructor with required fields.
     *
     * @param username the user's username
     * @param email the user's email
     * @param password the user's password
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    /**
     * Constructor with all fields.
     *
     * @param username the user's username
     * @param email the user's email
     * @param password the user's password
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param dateOfBirth the user's date of birth
     * @param height the user's height in cm
     * @param weight the user's weight in kg
     * @param fitnessGoal the user's fitness goal
     */
    public User(String username, String email, String password, String firstName,
                String lastName, Date dateOfBirth, double height, double weight,
                String fitnessGoal) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.height = height;
        this.weight = weight;
        this.fitnessGoal = fitnessGoal;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Getters and setters with Javadoc comments

    /**
     * Gets the user ID.
     * @return the ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the user ID.
     * @param id the ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the username.
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the email.
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password.
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the first name.
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     * @param firstName the first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name.
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name.
     * @param lastName the last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the date of birth.
     * @return the date of birth
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the date of birth.
     * @param dateOfBirth the date of birth
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Gets the height.
     * @return the height in cm
     */
    public double getHeight() {
        return height;
    }

    /**
     * Sets the height.
     * @param height the height in cm
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * Gets the weight.
     * @return the weight in kg
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Sets the weight.
     * @param weight the weight in kg
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Gets the fitness goal.
     * @return the fitness goal
     */
    public String getFitnessGoal() {
        return fitnessGoal;
    }

    /**
     * Sets the fitness goal.
     * @param fitnessGoal the fitness goal
     */
    public void setFitnessGoal(String fitnessGoal) {
        this.fitnessGoal = fitnessGoal;
    }

    /**
     * Gets the creation timestamp.
     * @return the creation date
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp.
     * @param createdAt the creation date
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the update timestamp.
     * @return the update date
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the update timestamp.
     * @param updatedAt the update date
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Gets the role of the user.
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the user.
     * @param role the role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Returns a string representation of the user.
     * @return user information as a string
     */
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", height=" + height +
                ", weight=" + weight +
                ", fitnessGoal='" + fitnessGoal + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
