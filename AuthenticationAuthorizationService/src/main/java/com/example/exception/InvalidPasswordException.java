package com.example.exception;

/**
 * Custom exception class for handling invalid password scenarios.
 * <p>
 * This exception is specifically used to indicate problems related to invalid passwords.
 * Extending IllegalArgumentException, this class signifies that the issue arises from an
 * inappropriate or unacceptable password argument passed to a method.
 * <p>
 * The constructor of the class allows for including a custom message, which can detail the
 * specific reason for the password being considered invalid (e.g., too short, lacks complexity).
 */
public class InvalidPasswordException extends IllegalArgumentException {
    public InvalidPasswordException(String msg) {
        super(msg);
    }
}
