package com.example.exception;

/**
 * Custom runtime exception for handling cases where a movement of money record is not found.
 */
public class MovementMoneyNotFoundException extends RuntimeException {
    public MovementMoneyNotFoundException(String message) {
        super(message);
    }
}
