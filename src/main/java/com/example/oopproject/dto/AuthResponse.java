package com.example.oopproject.dto;

/**
 * Data Transfer Object (DTO) for authentication responses.
 * <p>
 * Contains the token returned by the server after a successful login.
 */
public class AuthResponse {
    private String token;

    /**
     * Default constructor.
     */
    public AuthResponse() {
    }

    /**
     * Constructs an AuthResponse with the specified token.
     *
     * @param token the authentication token (e.g., JWT)
     */
    public AuthResponse(String token) {
        this.token = token;
    }

    /**
     * Gets the authentication token.
     *
     * @return the token string
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the authentication token.
     *
     * @param token the token string
     */
    public void setToken(String token) {
        this.token = token;
    }
}
