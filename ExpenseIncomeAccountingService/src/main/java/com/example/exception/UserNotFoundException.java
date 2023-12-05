package com.example.exception;

/**
 * Custom exception class for handling cases where a user is not found.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
