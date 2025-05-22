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
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Command line interface (CLI) component for the Fitness Tracker System.
 * Provides interactive console menus for users and admins to login, register,
 * browse workouts, and manage workouts and exercises.
 * <p>
 * This class implements Spring Boot's CommandLineRunner to launch the CLI
 * when the application starts.
 * </p>
 */

@Component
@ConditionalOnProperty(name = "org.example.oopproject1.cli.enabled", havingValue = "true", matchIfMissing = true)
@Profile("!test")
public class CommandLineInterface implements CommandLineRunner {

    private final UserService userService;
    private final WorkoutService workoutService;
    private final ExerciseService exerciseService;
    private final FitnessGoalService fitnessGoalService;
    private final Scanner scanner;
    private User currentUser;
    /**
     * Constructor to initialize services and scanner.
     *
     * @param userService        service to handle user operations
     * @param workoutService     service to handle workout operations
     * @param exerciseService    service to handle exercise operations
     * @param fitnessGoalService service to handle fitness goal operations
     */
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
    /**
     * Entry point for the CLI. Displays the main menu and processes user input
     * until the user exits.
     *
     * @param args command line arguments (not used)
     * @throws Exception if an error occurs during input processing
     */
    @Override
    public void run(String... args) throws Exception {

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
                    adminLogin();
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
    /**
     * Displays the main menu options to the console.
     */
    private void displayMainMenu() {
        System.out.println("\n===== FITNESS TRACKER SYSTEM =====");
        System.out.println("1. Login");
        System.out.println("2. Register New User");
        System.out.println("3. Browse Workouts (Guest)");
        System.out.println("4. Admin Login");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    /**
     * Reads and validates the user's menu choice input.
     *
     * @param maxChoice the maximum valid choice number
     * @return a valid integer choice between 1 and maxChoice
     */
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

    /**
     * Handles user login by prompting for username and password,
     * then verifying credentials and displaying the appropriate menu.
     */
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
                    userMenu();
                }
            } else {
                System.out.println("Incorrect password. Login failed.");
            }
        } catch (Exception e) {
            System.out.println("Username not found. Please check your username or register a new account.");
        }
    }

    /**
     * Handles admin login by prompting for the admin password.
     * Displays the admin menu on successful login.
     */
    private void adminLogin() {
        System.out.println("\n===== ADMIN LOGIN =====");
        System.out.print("Enter admin password: ");
        String adminPassword = scanner.nextLine();

        if ("1234".equals(adminPassword)) {
            System.out.println("Admin login successful!");
            adminMenu();
        } else {
            System.out.println("Incorrect admin password. Access denied.");
        }
    }

    /**
     * Displays the admin menu with options to manage workouts and exercises,
     * and handles admin input.
     */
    private void adminMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n===== ADMIN MENU =====");
            System.out.println("1. View All Workouts");
            System.out.println("2. Add New Workout");
            System.out.println("3. Update Workout");
            System.out.println("4. Delete Workout");
            System.out.println("5. Add New Exercise");
            System.out.println("6. Update Exercise");
            System.out.println("7. Delete Exercise");
            System.out.println("8. Advanced Workout Options");
            System.out.println("9. Advanced Exercise Options");
            System.out.println("10. Logout");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice(10);
            switch (choice) {
                case 1:
                    browseWorkouts();
                    break;
                case 2:
                    addWorkout();
                    break;
                case 3:
                    updateWorkout();
                    break;
                case 4:
                    deleteWorkout();
                    break;
                case 5:
                    addExercise();
                    break;
                case 6:
                    updateExercise();
                    break;
                case 7:
                    deleteExercise();
                    break;
                case 8:
                    advancedWorkoutOptions();
                    break;
                case 9:
                    advancedExerciseOptions();
                    break;
                case 10:
                    System.out.println("Logging out.");
                    currentUser = null;
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    /**
     * Displays advanced workout options menu and handles user input.
     */
    private void advancedWorkoutOptions() {
        boolean back = false;

        while (!back) {
            System.out.println("\n===== ADVANCED WORKOUT OPTIONS =====");
            System.out.println("1. View Paginated Workouts");
            System.out.println("2. Filter Workouts by Date Range");
            System.out.println("3. Search Workouts by Type");
            System.out.println("4. Back to Admin Menu");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice(4);
            switch (choice) {
                case 1:
                    paginatedWorkouts();
                    break;
                case 2:
                    filterWorkoutsByDateRange();
                    break;
                case 3:
                    searchWorkoutsByType();
                    break;
                case 4:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
    /**
     * Displays workouts in a paginated and sorted manner based on user input.
     */

    private void paginatedWorkouts() {
        System.out.println("\n===== PAGINATED WORKOUTS =====");


        System.out.print("Enter page number (starting from 0): ");
        int page = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter page size: ");
        int size = Integer.parseInt(scanner.nextLine());

        System.out.println("Sort by:");
        System.out.println("1. Date");
        System.out.println("2. Workout Type");
        System.out.println("3. Duration");
        System.out.println("4. Calories Burned");
        System.out.print("Enter your choice: ");
        int sortChoice = getUserChoice(4);

        String sortBy;
        switch (sortChoice) {
            case 1:
                sortBy = "date";
                break;
            case 2:
                sortBy = "workoutType";
                break;
            case 3:
                sortBy = "duration";
                break;
            case 4:
                sortBy = "caloriesBurned";
                break;
            default:
                sortBy = "date";
        }

        // Get sort direction
        System.out.println("Sort direction:");
        System.out.println("1. Ascending");
        System.out.println("2. Descending");
        System.out.print("Enter your choice: ");
        int dirChoice = getUserChoice(2);

        String sortDir = (dirChoice == 1) ? "asc" : "desc";

        try {

            List<Workout> workouts = workoutService.getPaginatedWorkouts(page, size, sortBy, sortDir);

            if (workouts.isEmpty()) {
                System.out.println("No workouts found for this page.");
                return;
            }

            System.out.println("\nPage " + page + " (Size: " + size + ", Sorted by: " + sortBy + " " + sortDir + ")");
            System.out.println("--------------------------------------------------");

            for (int i = 0; i < workouts.size(); i++) {
                Workout workout = workouts.get(i);
                System.out.println((i + 1) + ". " + workout.getWorkoutType() +
                        " - Duration: " + workout.getDuration() + " min, " +
                        "Calories: " + workout.getCaloriesBurned() +
                        ", Date: " + dateFormat.format(workout.getDate()));
            }

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();

        } catch (Exception e) {
            System.out.println("Error retrieving paginated workouts: " + e.getMessage());
        }
    }
    /**
     * Prompts user to enter a date range and filters workouts within that range.
     */
    private void filterWorkoutsByDateRange() {
        System.out.println("\n===== FILTER WORKOUTS BY DATE RANGE =====");

        System.out.print("Enter start date (yyyy-MM-dd): ");
        String startDate = scanner.nextLine();

        System.out.print("Enter end date (yyyy-MM-dd): ");
        String endDate = scanner.nextLine();

        try {
                List<Workout> workouts = workoutService.filterByDateRange(startDate, endDate);

            if (workouts.isEmpty()) {
                System.out.println("No workouts found in this date range.");
                return;
            }

            System.out.println("\nWorkouts between " + startDate + " and " + endDate + ":");
            System.out.println("--------------------------------------------------");

            for (int i = 0; i < workouts.size(); i++) {
                Workout workout = workouts.get(i);
                System.out.println((i + 1) + ". " + workout.getWorkoutType() +
                        " - Duration: " + workout.getDuration() + " min, " +
                        "Calories: " + workout.getCaloriesBurned() +
                        ", Date: " + dateFormat.format(workout.getDate()));
            }

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();

        } catch (Exception e) {
            System.out.println("Error filtering workouts: " + e.getMessage());
        }
    }
    /**
     * Prompts user to enter a workout type and searches for workouts matching that type.
     */
    private void searchWorkoutsByType() {
        System.out.println("\n===== SEARCH WORKOUTS BY TYPE =====");

        System.out.print("Enter workout type to search: ");
        String workoutType = scanner.nextLine();

        System.out.print("Enter user ID (leave blank to search all workouts): ");
        String userId = scanner.nextLine();

        try {

            List<Workout> workouts = workoutService.searchWorkouts(workoutType, userId.isEmpty() ? null : userId);

            if (workouts.isEmpty()) {
                System.out.println("No workouts found matching the criteria.");
                return;
            }

            System.out.println("\nWorkouts of type '" + workoutType + "':");
            System.out.println("--------------------------------------------------");

            for (int i = 0; i < workouts.size(); i++) {
                Workout workout = workouts.get(i);
                System.out.println((i + 1) + ". " + workout.getWorkoutType() +
                        " - Duration: " + workout.getDuration() + " min, " +
                        "Calories: " + workout.getCaloriesBurned() +
                        ", Date: " + dateFormat.format(workout.getDate()));
            }

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();

        } catch (Exception e) {
            System.out.println("Error searching workouts: " + e.getMessage());
        }
    }
    /**
     * Displays advanced exercise options menu and handles user input.
     */
    private void advancedExerciseOptions() {
        boolean back = false;

        while (!back) {
            System.out.println("\n===== ADVANCED EXERCISE OPTIONS =====");
            System.out.println("1. View Paginated Exercises");
            System.out.println("2. Filter & Search Exercises");
            System.out.println("3. Back to Admin Menu");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice(3);
            switch (choice) {
                case 1:
                    paginatedExercises();
                    break;
                case 2:
                    filterAndSearchExercises();
                    break;
                case 3:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void paginatedExercises() {
        System.out.println("\n===== PAGINATED EXERCISES =====");

        // Get page number
        System.out.print("Enter page number (starting from 0): ");
        int page = Integer.parseInt(scanner.nextLine());


        System.out.print("Enter page size: ");
        int size = Integer.parseInt(scanner.nextLine());

        System.out.println("Sort by:");
        System.out.println("1. Name");
        System.out.println("2. Sets");
        System.out.println("3. Reps");
        System.out.println("4. Weight");
        System.out.print("Enter your choice: ");
        int sortChoice = getUserChoice(4);

        String sortBy;
        switch (sortChoice) {
            case 1:
                sortBy = "name";
                break;
            case 2:
                sortBy = "sets";
                break;
            case 3:
                sortBy = "reps";
                break;
            case 4:
                sortBy = "weight";
                break;
            default:
                sortBy = "name";
        }

        // Get sort direction
        System.out.println("Sort direction:");
        System.out.println("1. Ascending");
        System.out.println("2. Descending");
        System.out.print("Enter your choice: ");
        int dirChoice = getUserChoice(2);

        String direction = (dirChoice == 1) ? "asc" : "desc";

        try {

            Page<Exercise> exercisePage = exerciseService.getExercisesPaginated(page, size, sortBy, direction);
            List<Exercise> exercises = exercisePage.getContent();

            if (exercises.isEmpty()) {
                System.out.println("No exercises found for this page.");
                return;
            }

            System.out.println("\nPage " + page + " (Size: " + size + ", Sorted by: " + sortBy + " " + direction + ")");
            System.out.println("--------------------------------------------------");
            System.out.println("Total pages: " + exercisePage.getTotalPages());
            System.out.println("Total elements: " + exercisePage.getTotalElements());

            for (int i = 0; i < exercises.size(); i++) {
                Exercise exercise = exercises.get(i);
                System.out.println((i + 1) + ". " + exercise.getName() +
                        " - Sets: " + exercise.getSets() +
                        ", Reps: " + exercise.getReps() +
                        ", Weight: " + exercise.getWeight() + " kg");
            }

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();

        } catch (Exception e) {
            System.out.println("Error retrieving paginated exercises: " + e.getMessage());
        }
    }

    private void filterAndSearchExercises() {
        System.out.println("\n===== FILTER & SEARCH EXERCISES =====");

        System.out.print("Enter exercise name (or part of name, leave blank to skip): ");
        String name = scanner.nextLine();

        System.out.print("Enter reps (leave blank to skip): ");
        String repsInput = scanner.nextLine();
        Integer reps = repsInput.isEmpty() ? null : Integer.parseInt(repsInput);

        System.out.print("Enter sets (leave blank to skip): ");
        String setsInput = scanner.nextLine();
        Integer sets = setsInput.isEmpty() ? null : Integer.parseInt(setsInput);

        System.out.print("Enter workout ID (leave blank to skip): ");
        String workoutId = scanner.nextLine();
        if (workoutId.isEmpty()) workoutId = null;

        try {
            // Call a method in exerciseService to filter exercises
            // This method needs to be implemented in your ExerciseService
            List<Exercise> exercises = exerciseService.getExercisesByFilter(reps, sets, name.isEmpty() ? null : name, workoutId);

            if (exercises.isEmpty()) {
                System.out.println("No exercises found matching the criteria.");
                return;
            }

            System.out.println("\nMatching exercises:");
            System.out.println("--------------------------------------------------");

            for (int i = 0; i < exercises.size(); i++) {
                Exercise exercise = exercises.get(i);
                System.out.println((i + 1) + ". " + exercise.getName() +
                        " - Sets: " + exercise.getSets() +
                        ", Reps: " + exercise.getReps() +
                        ", Weight: " + exercise.getWeight() + " kg" +
                        (workoutId != null ? "" : ", Workout ID: " + exercise.getWorkoutId()));
            }

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();

        } catch (Exception e) {
            System.out.println("Error filtering exercises: " + e.getMessage());
        }
    }
    /**
     * Allows the admin to add a new workout by entering workout details.
     */
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
    /**
     * Allows the admin to delete a workout by its ID.
     */
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
    /**
     * Allows the admin to update an existing workout's details.
     */
    private void updateWorkout() {
        System.out.print("\nEnter workout ID to update: ");
        String id = scanner.nextLine();
        try {
            Workout existingWorkout = workoutService.getWorkoutById(id);
            if (existingWorkout != null) {
                System.out.print("Enter new workout type (current: " + existingWorkout.getWorkoutType() + "): ");
                String workoutType = scanner.nextLine();
                if (!workoutType.isEmpty()) {
                    existingWorkout.setWorkoutType(workoutType);
                }

                System.out.print("Enter new duration (minutes) (current: " + existingWorkout.getDuration() + "): ");
                String durationInput = scanner.nextLine();
                if (!durationInput.isEmpty()) {
                    existingWorkout.setDuration(Integer.parseInt(durationInput));
                }

                System.out.print("Enter new calories burned (current: " + existingWorkout.getCaloriesBurned() + "): ");
                String caloriesInput = scanner.nextLine();
                if (!caloriesInput.isEmpty()) {
                    existingWorkout.setCaloriesBurned(Integer.parseInt(caloriesInput));
                }

                System.out.print("Enter new date (yyyy-MM-dd) (current: " + dateFormat.format(existingWorkout.getDate()) + "): ");
                String dateInput = scanner.nextLine();
                if (!dateInput.isEmpty()) {
                    try {
                        Date newDate = dateFormat.parse(dateInput);
                        existingWorkout.setDate(newDate);
                    } catch (ParseException e) {
                        System.out.println("Invalid date format. Keeping the current date.");
                    }
                }

                workoutService.updateWorkout(id, existingWorkout);
                System.out.println("Workout updated successfully.");
            } else {
                System.out.println("Workout not found.");
            }
        } catch (Exception e) {
            System.out.println("Error updating workout: " + e.getMessage());
        }
    }
    /**
     * Allows the admin to add a new exercise by entering exercise details.
     */
    private void addExercise() {
        System.out.println("\n===== ADD NEW EXERCISE =====");
        Exercise exercise = new Exercise("Push Ups", 10, 3, "workout123");

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
    /**
     * Allows the admin to update an existing exercise's details.
     */
    private void updateExercise() {
        System.out.print("\nEnter exercise ID to update: ");
        String id = scanner.nextLine();
        try {
            Exercise existingExercise = exerciseService.getExerciseById(id);
            if (existingExercise != null) {
                System.out.print("Enter new exercise name (current: " + existingExercise.getName() + "): ");
                String name = scanner.nextLine();
                if (!name.isEmpty()) {
                    existingExercise.setName(name);
                }

                System.out.print("Enter new sets (current: " + existingExercise.getSets() + "): ");
                String setsInput = scanner.nextLine();
                if (!setsInput.isEmpty()) {
                    existingExercise.setSets(Integer.parseInt(setsInput));
                }

                System.out.print("Enter new reps (current: " + existingExercise.getReps() + "): ");
                String repsInput = scanner.nextLine();
                if (!repsInput.isEmpty()) {
                    existingExercise.setReps(Integer.parseInt(repsInput));
                }

                System.out.print("Enter new weight (kg) (current: " + existingExercise.getWeight() + "): ");
                String weightInput = scanner.nextLine();
                if (!weightInput.isEmpty()) {
                    existingExercise.setWeight(Double.parseDouble(weightInput));
                }

                exerciseService.updateExercise(id, existingExercise);
                System.out.println("Exercise updated successfully.");
            } else {
                System.out.println("Exercise not found.");
            }
        } catch (Exception e) {
            System.out.println("Error updating exercise: " + e.getMessage());
        }
    }
    /**
     * Allows the admin to delete an exercise by its ID.
     */
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
    /**
     * Displays the user menu with workout browsing and personal options.
     */
    private void userMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n===== USER MENU =====");
            System.out.println("1. View All Workouts");
            System.out.println("2. Advanced Workout Options"); // New option for pagination/filtering
            System.out.println("3. Advanced Exercise Options"); // New option for pagination/filtering
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice(4);
            switch (choice) {
                case 1:
                    browseWorkouts();
                    break;
                case 2:
                    advancedWorkoutOptions(); // Reuse the same method as admin
                    break;
                case 3:
                    advancedExerciseOptions(); // Reuse the same method as admin
                    break;
                case 4:
                    System.out.println("Logging out.");
                    currentUser = null;
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
    /**
     * Allows a user to register by entering username, password, and role.
     * Registers a new user in the system.
     */
    private void registerNewUser() {
        System.out.println("\n===== REGISTER NEW USER =====");
        User newUser = new User();

        System.out.print("Enter username: ");
        newUser.setUsername(scanner.nextLine());

        System.out.print("Enter password: ");
        newUser.setPassword(scanner.nextLine());

        System.out.print("Enter email: ");
        newUser.setEmail(scanner.nextLine());

        System.out.print("Enter first name: ");
        newUser.setFirstName(scanner.nextLine());

        System.out.print("Enter last name: ");
        newUser.setLastName(scanner.nextLine());

        System.out.print("Enter date of birth (yyyy-MM-dd): ");
        try {
            Date dob = dateFormat.parse(scanner.nextLine());
            newUser.setDateOfBirth(dob);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Using current date as default.");
            newUser.setDateOfBirth(new Date());
        }

        System.out.print("Enter height (cm): ");
        String heightInput = scanner.nextLine();
        if (!heightInput.isEmpty()) {
            newUser.setHeight(Double.parseDouble(heightInput));
        }

        System.out.print("Enter weight (kg): ");
        String weightInput = scanner.nextLine();
        if (!weightInput.isEmpty()) {
            newUser.setWeight(Double.parseDouble(weightInput));
        }

        System.out.print("Enter fitness goal (e.g., Weight Loss, Muscle Gain, etc.): ");
        newUser.setFitnessGoal(scanner.nextLine());

        // Set default role to "user"
        newUser.setRole("user");

        // Set creation and update timestamps
        Date now = new Date();
        newUser.setCreatedAt(now);
        newUser.setUpdatedAt(now);

        try {
            userService.createUser(newUser);
            System.out.println("User registered successfully! You can now login.");
        } catch (Exception e) {
            System.out.println("Failed to register user: " + e.getMessage());
        }
    }
    /**
     * Displays all workouts available for browsing.
     */
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
    /**
     * Displays the workouts details.
     */
    private void displayWorkoutDetails(Workout workout) {
        System.out.println("\n===== WORKOUT DETAILS =====");
        System.out.println("Type: " + workout.getWorkoutType());
        System.out.println("Duration: " + workout.getDuration() + " minutes");
        System.out.println("Calories Burned: " + workout.getCaloriesBurned());
        System.out.println("Date: " + dateFormat.format(workout.getDate()));
    }
}