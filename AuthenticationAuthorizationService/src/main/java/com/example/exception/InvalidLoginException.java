package com.example.exception;

/**
 * Custom exception class representing an invalid login attempt.
 * <p>
 * This exception is thrown when a user attempts to log in with invalid credentials.
 * It extends IllegalArgumentException for signaling that an argument passed to a method is inappropriate.
 * <p>
 * The constructor of this class allows for a custom message to be passed, which can
 * provide more detailed information about why the login attempt was considered invalid.
 */
public class InvalidLoginException extends IllegalArgumentException {
    public InvalidLoginException(String msg) {
        super(msg);
    }
}
