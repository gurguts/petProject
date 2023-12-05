package com.example.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

/**
 * This class extends AuthenticationException from Spring Security and is used specifically
 * for issues related to JWT authentication. It provides additional context by including an
 * HttpStatus, allowing for more precise error handling and response status setting in the
 * application's security context.
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
