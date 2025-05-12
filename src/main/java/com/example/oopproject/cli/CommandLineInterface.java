package com.example.oopproject.cli;

import com.example.oopproject.models.Exercise;
import com.example.oopproject.models.User;
import com.example.oopproject.models.Workout;
import com.example.oopproject.services.ExerciseService;
import com.example.oopproject.services.FitnessGoalService;
import com.example.oopproject.services.UserService;
import com.example.oopproject.services.WorkoutService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
            int choice = getUserChoice(5);

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
                    adminLogin();  // Admin login prompt
                    break;
                case 5:
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
        System.out.println("4. Admin Login");
        System.out.println("5. Exit");
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

            if (user.getPassword().equals(password)) {
                currentUser = user;
                System.out.println("Login successful! Welcome, " + user.getUsername() + "!");

                if ("admin".equalsIgnoreCase(user.getRole())) {
                    adminMenu();
                } else {
                    userMenu(); // Currently empty
                }
            } else {
                System.out.println("Incorrect password. Login failed.");
            }
        } catch (Exception e) {
            System.out.println("Username not found. Please check your username or register a new account.");
        }
    }

    private void adminLogin() {
        System.out.println("\n===== ADMIN LOGIN =====");
        System.out.print("Enter admin password: ");
        String adminPassword = scanner.nextLine();

        if ("1234".equals(adminPassword)) {
            System.out.println("Admin login successful!");
            adminMenu();  // Show admin menu
        } else {
            System.out.println("Incorrect admin password. Access denied.");
        }
    }

    private void adminMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n===== ADMIN MENU =====");
            System.out.println("1. View All Workouts");
            System.out.println("2. Add New Workout");
            System.out.println("3. Delete Workout");
            System.out.println("4. Add New Exercise");
            System.out.println("5. Delete Exercise");
            System.out.println("6. Logout");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice(6);
            switch (choice) {
                case 1:
                    browseWorkouts();
                    break;
                case 2:
                    addWorkout();
                    break;
                case 3:
                    deleteWorkout();
                    break;
                case 4:
                    addExercise();
                    break;
                case 5:
                    deleteExercise();
                    break;
                case 6:
                    System.out.println("Logging out.");
                    currentUser = null;
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void addWorkout() {
        System.out.println("\n===== ADD NEW WORKOUT =====");
        Workout workout = new Workout();

        System.out.print("Enter workout type: ");
        workout.setWorkoutType(scanner.nextLine());

        System.out.print("Enter duration (minutes): ");
        workout.setDuration(Integer.parseInt(scanner.nextLine()));

        System.out.print("Enter calories burned: ");
        workout.setCaloriesBurned(Integer.parseInt(scanner.nextLine()));

        System.out.print("Enter date (yyyy-MM-dd): ");
        try {
            Date date = dateFormat.parse(scanner.nextLine());
            workout.setDate(date);
        } catch (ParseException e) {
            workout.setDate(new Date());
        }

        workoutService.createWorkout(workout);
        System.out.println("Workout added successfully.");
    }

    private void deleteWorkout() {
        System.out.print("\nEnter workout ID to delete: ");
        String id = scanner.nextLine();
        try {
            workoutService.deleteWorkout(id);
            System.out.println("Workout deleted successfully.");
        } catch (Exception e) {
            System.out.println("Failed to delete workout: " + e.getMessage());
        }
    }

    private void addExercise() {
        System.out.println("\n===== ADD NEW EXERCISE =====");
        Exercise exercise = new Exercise();

        System.out.print("Enter exercise name: ");
        exercise.setName(scanner.nextLine());

        System.out.print("Enter sets: ");
        exercise.setSets(Integer.parseInt(scanner.nextLine()));

        System.out.print("Enter reps: ");
        exercise.setReps(Integer.parseInt(scanner.nextLine()));

        System.out.print("Enter weight (kg): ");
        exercise.setWeight(Double.parseDouble(scanner.nextLine()));

        exerciseService.createExercise(exercise);
        System.out.println("Exercise added successfully.");
    }

    private void deleteExercise() {
        System.out.print("\nEnter exercise ID to delete: ");
        String id = scanner.nextLine();
        try {
            exerciseService.deleteExercise(id);
            System.out.println("Exercise deleted successfully.");
        } catch (Exception e) {
            System.out.println("Failed to delete exercise: " + e.getMessage());
        }
    }

    private void userMenu() {
        // Optional: You can add user-specific menu here in the future
    }

    private void registerNewUser() {
        // Unchanged registration logic
        // ...
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

    }
}