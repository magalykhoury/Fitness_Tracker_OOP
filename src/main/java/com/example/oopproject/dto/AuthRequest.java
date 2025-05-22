package com.example.oopproject.dto;

/**
 * Data Transfer Object (DTO) for user authentication requests.
 * <p>
 * Contains the username and password sent by the client
 * when attempting to log in.
 */
public class AuthRequest {
    private String username;
    private String password;

    /**
     * Default constructor.
     */
    public AuthRequest() {
    }

    /**
     * Constructs an AuthRequest with the specified username and password.
     *
     * @param username the username or email of the user
     * @param password the user's password
     */
    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the username.
     *
     * @return the username or email
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the username or email
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password.
     *
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
