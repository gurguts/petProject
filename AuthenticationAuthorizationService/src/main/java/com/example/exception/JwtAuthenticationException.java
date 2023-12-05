package com.example.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

/**
 * Custom exception class for handling JWT authentication errors.
 * <p>
 * This exception is thrown to signal issues related to JWT (JSON Web Token) authentication. It extends
 * AuthenticationException from Spring Security, making it suitable for use within the security
 * framework of a Spring application.
 * <p>
 * In addition to the standard exception message, this class includes an HTTP status code. This
 * allows for more granular control over the HTTP response returned to the client in the event
 * of an authentication failure.
 * <p>
 * The class provides two constructors:
 * 1. A constructor accepting only a message, which can be used when the default HTTP status is sufficient.
 * 2. A constructor accepting both a message and an HTTP status code, allowing customization of the response status.
 * <p>
 * The @Getter annotation from Lombok automatically generates a getter method for the httpStatus field.
 */
@Getter
public class JwtAuthenticationException extends AuthenticationException {
    private HttpStatus httpStatus;

    public JwtAuthenticationException(String msg) {
        super(msg);
    }

    public JwtAuthenticationException(String msg, HttpStatus httpStatus) {
        super(msg);
        this.httpStatus = httpStatus;
    }
}
