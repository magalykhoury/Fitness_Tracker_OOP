package com.example.oopproject.cli;


import com.example.oopproject.models.Exercise;
import com.example.oopproject.models.User;
import com.example.oopproject.models.Workout;
import com.example.oopproject.services.ExerciseService;
import com.example.oopproject.services.FitnessGoalService;
import com.example.oopproject.services.UserService;
import com.example.oopproject.services.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@Component
@ConditionalOnProperty(name = "org.example.oopproject1.cli.enabled", havingValue = "true", matchIfMissing = true)
public class CommandLineInterface implements CommandLineRunner {

    private final UserService userService;
    private final WorkoutService workoutService;
    private final ExerciseService exerciseService;
    private final FitnessGoalService fitnessGoalService;
    private final Scanner scanner;
    private User currentUser;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    public CommandLineInterface(UserService userService,
                                WorkoutService workoutService,
                                ExerciseService exerciseService,
                                FitnessGoalService fitnessGoalService) {
        this.userService = userService;
        this.workoutService = workoutService;
        this.exerciseService = exerciseService;
        this.fitnessGoalService = fitnessGoalService;
        this.scanner = new Scanner(System.in);
        this.currentUser = null;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello");
        boolean exit = false;

        while (!exit) {
            displayMainMenu();
            int choice = getUserChoice(4);

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    registerNewUser();
                    break;
                case 3:
                    browseWorkouts();
                    break;
                case 4:
                    exit = true;
                    System.out.println("Exiting application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("\n===== FITNESS TRACKER SYSTEM =====");
        System.out.println("1. Login");
        System.out.println("2. Register New User");
        System.out.println("3. Browse Workouts (Guest)");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private int getUserChoice(int maxChoice) {
        int choice = -1;
        boolean validInput = false;

        while (!validInput) {
            try {
                String input = scanner.nextLine();
                choice = Integer.parseInt(input);

                if (choice >= 1 && choice <= maxChoice) {
                    validInput = true;
                } else {
                    System.out.print("Invalid choice. Please enter a number between 1 and "
                            + maxChoice + ": ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }

        return choice;
    }

    private void login() {
        System.out.println("\n===== LOGIN =====");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            User user = userService.getUserByUsername(username);

            // In a real application, you would use password hashing
            if (user.getPassword().equals(password)) {
                currentUser = user;
                System.out.println("Login successful! Welcome, " + user.getUsername() + "!");
                userMenu();
            } else {
                System.out.println("Incorrect password. Login failed.");
            }
        } catch (Exception e) {
            System.out.println("Username not found. Please check your username or register a new account.");
        }
    }

    private void userMenu() {
    }

    private void registerNewUser() {
        System.out.println("\n===== REGISTER NEW USER =====");

        User newUser = new User();

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        // Check if username already exists
        try {
            userService.getUserByUsername(username);
            System.out.println("Username already exists. Please choose a different username.");
            return;
        } catch (Exception e) {
            // Username is available
            newUser.setUsername(username);
        }

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        // Check if email already exists
        try {
            userService.getUserByEmail(email);
            System.out.println("Email already exists. Please use a different email.");
            return;
        } catch (Exception e) {
            // Email is available
            newUser.setEmail(email);
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        newUser.setPassword(password);

        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        newUser.setFirstName(firstName);

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        newUser.setLastName(lastName);

        System.out.print("Enter date of birth (YYYY-MM-DD): ");
        String dobString = scanner.nextLine();
        try {
            Date dob = dateFormat.parse(dobString);
            newUser.setDateOfBirth(dob);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Setting to current date.");
            newUser.setDateOfBirth(new Date());
        }

        System.out.print("Enter height in cm: ");
        try {
            double height = Double.parseDouble(scanner.nextLine());
            newUser.setHeight(height);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Setting height to 0.");
            newUser.setHeight(0);
        }

        System.out.print("Enter weight in kg: ");
        try {
            double weight = Double.parseDouble(scanner.nextLine());
            newUser.setWeight(weight);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Setting weight to 0.");
            newUser.setWeight(0);
        }

        System.out.println("Choose fitness goal:");
        System.out.println("1. Weight Loss");
        System.out.println("2. Muscle Gain");
        System.out.println("3. Endurance");
        System.out.println("4. General Fitness");
        System.out.print("Enter your choice: ");

        int choice = getUserChoice(4);
        String fitnessGoal;

        switch (choice) {
            case 1:
                fitnessGoal = "weight loss";
                break;
            case 2:
                fitnessGoal = "muscle gain";
                break;
            case 3:
                fitnessGoal = "endurance";
                break;
            case 4:
                fitnessGoal = "general fitness";
                break;
            default:
                fitnessGoal = "general fitness";
        }

        newUser.setFitnessGoal(fitnessGoal);

        try {
            User createdUser = userService.createUser(newUser);
            System.out.println("User registered successfully!");
            System.out.println("Your account has been created with ID: " + createdUser.getId());
            System.out.println("You can now login with your username and password.");
        } catch (Exception e) {
            System.out.println("Error registering user: " + e.getMessage());
        }
    }

    private void browseWorkouts() {
        System.out.println("\n===== BROWSE WORKOUTS =====");

        try {
            List<Workout> workouts = workoutService.getAllWorkouts();

            if (workouts.isEmpty()) {
                System.out.println("No workouts found in the system.");
                return;
            }

            System.out.println("Available workouts:");
            for (int i = 0; i < workouts.size(); i++) {
                Workout workout = workouts.get(i);
                System.out.println((i + 1) + ". " + workout.getWorkoutType() +
                        " - Duration: " + workout.getDuration() + " min, " +
                        "Calories: " + workout.getCaloriesBurned());
            }

            System.out.println("\nEnter workout number to view details (or 0 to go back): ");
            int choice = getUserChoice(workouts.size() + 1) - 1;

            if (choice >= 0 && choice < workouts.size()) {
                displayWorkoutDetails(workouts.get(choice));
            }
        } catch (Exception e) {
            System.out.println("Error retrieving workouts: " + e.getMessage());
        }
    }

    private void displayWorkoutDetails(Workout workout) {
        System.out.println("\n===== WORKOUT DETAILS =====");
        System.out.println("Type: " + workout.getWorkoutType());
        System.out.println("Duration: " + workout.getDuration() + " minutes");
        System.out.println("Calories Burned: " + workout.getCaloriesBurned());
        System.out.println("Date: " + dateFormat.format(workout.getDate()));

        List<Exercise> exercises = workout.getExercises();
        if (exercises != null && !exercises.isEmpty()) {
            System.out.println("\nExercises:");
            for (int i = 0; i < exercises.size(); i++) {
                Exercise exercise = exercises.get(i);
                System.out.println((i + 1) + ". " + exercise.getName());
                System.out.println("   Sets: " + exercise.getSets() + ", Reps: " + exercise.getReps());
                if (exercise.getWeight() > 0) {
                    System.out.println("   Weight: " + exercise.getWeight() + " kg");
                }
            }
        } else {
            System.out.println("\nNo exercises included in this workout.");
        }

    }
}
