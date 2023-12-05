package com.example.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * This controller advice class is responsible for handling exceptions globally across the entire application.
 * It catches specific types of exceptions and provides custom responses, ensuring a consistent error handling
 * strategy.
 * <p>
 * By centralizing exception handling in this manner, the application can maintain clean controller methods
 * and ensure that all exceptions are handled consistently and gracefully.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * This method is an exception handler that specifically targets HttpMessageNotReadableException,
     * which typically occurs when there is an issue with parsing HTTP requests, such as malformed JSON.
     * It provides a standard way to respond to these exceptions across the application.
     * <p>
     * The method returns a ResponseEntity with a BAD_REQUEST (400) status, indicating that the client
     * sent a request that the server could not understand. The response body includes a message explaining
     * the issue, along with details of the exception, aiding in diagnosing the parsing error.
     *
     * @param ex The HttpMessageNotReadableException instance caught by this handler.
     * @return A ResponseEntity with the BAD_REQUEST status and a descriptive error message.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request: Unable to parse JSON. " + ex);
    }

    /**
     * This method serves as a catch-all exception handler for any type of Exception that is not
     * specifically handled by other @ExceptionHandler methods in the application. It provides a
     * general response for unanticipated exceptions, ensuring that the application does not crash
     * and instead returns a controlled response.
     * <p>
     * The handler returns a ResponseEntity with an INTERNAL_SERVER_ERROR (500) status, indicating
     * that an unexpected condition was encountered on the server. The response body includes a
     * generic error message, along with details of the exception, which can be useful for debugging.
     *
     * @param ex The Exception instance caught by this handler.
     * @return A ResponseEntity with the INTERNAL_SERVER_ERROR status and a descriptive error message.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleRuntimeException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error: " + ex);
    }
}