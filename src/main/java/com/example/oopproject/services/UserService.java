package com.example.oopproject.services;

import com.example.oopproject.exceptions.BadRequestException;
import com.example.oopproject.exceptions.ResourceNotFoundException;
import com.example.oopproject.models.User;
import com.example.oopproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Service class for managing User entities.
 * Provides methods for CRUD operations and user lookups by different criteria.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * Constructor injecting UserRepository.
     * @param userRepository repository for User persistence
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all users.
     * @return list of all User entities
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves all users with pagination.
     * @param pageable pagination information
     * @return paginated list of User entities
     */
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * Finds a user by their ID.
     * @param id the user ID
     * @return the User entity if found
     * @throws ResourceNotFoundException if user is not found
     */
    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    /**
     * Finds a user by their username.
     * @param username the username
     * @return the User entity if found
     * @throws ResourceNotFoundException if user is not found
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    /**
     * Finds a user by their email.
     * @param email the email
     * @return the User entity if found
     * @throws ResourceNotFoundException if user is not found
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    /**
     * Creates a new user after checking for unique username and email.
     * Note: Password is stored in plaintext here; should be hashed in production.
     * @param user the User entity to create
     * @return the created User entity
     * @throws BadRequestException if username or email already exists
     */
    public User createUser(User user) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BadRequestException("Username already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        // For now, storing password as plaintext; hash in real app
        return userRepository.save(user);
    }

    /**
     * Updates an existing user by ID.
     * Validates changes to username and email for uniqueness.
     * Updates password only if provided.
     * Updates the updatedAt timestamp.
     * @param id the user ID
     * @param userDetails the updated User data
     * @return the updated User entity
     * @throws BadRequestException if new username or email already exists
     */
    public User updateUser(String id, User userDetails) {
        User user = getUserById(id);

        // Check if username is changed and unique
        if (!user.getUsername().equals(userDetails.getUsername()) &&
                userRepository.existsByUsername(userDetails.getUsername())) {
            throw new BadRequestException("Username already exists");
        }

        // Check if email is changed and unique
        if (!user.getEmail().equals(userDetails.getEmail()) &&
                userRepository.existsByEmail(userDetails.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());

        // Update password only if provided (plaintext here)
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(userDetails.getPassword());
        }

        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setDateOfBirth(userDetails.getDateOfBirth());
        user.setHeight(userDetails.getHeight());
        user.setWeight(userDetails.getWeight());
        user.setFitnessGoal(userDetails.getFitnessGoal());
        user.setUpdatedAt(new Date());

        return userRepository.save(user);
    }

    /**
     * Deletes a user by ID.
     * @param id the user ID
     */
    public void deleteUser(String id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}
