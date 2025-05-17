package com.example.oopproject.exceptions;

import java.util.Date;
import java.util.Map;

/**
 * Represents a response body for validation errors.
 * Includes a timestamp, general message, and field-specific validation errors.
 */
public class ValidationErrorResponse {
    private Date timestamp;
    private String message;
    private Map<String, String> errors;

    /**
     * Constructs a new ValidationErrorResponse with the specified details.
     *
     * @param timestamp the time the error occurred
     * @param message a general error message
     * @param errors a map containing field names and their corresponding validation error messages
     */
    public ValidationErrorResponse(Date timestamp, String message, Map<String, String> errors) {
        this.timestamp = timestamp;
        this.message = message;
        this.errors = errors;
    }

    /**
     * Gets the timestamp when the error occurred.
     *
     * @return the timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the general error message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the map of field-specific validation error messages.
     *
     * @return a map of field names to error messages
     */
    public Map<String, String> getErrors() {
        return errors;
    }
}
