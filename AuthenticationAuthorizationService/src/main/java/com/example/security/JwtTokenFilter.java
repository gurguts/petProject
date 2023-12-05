package com.example.security;

import com.example.exception.JwtAuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * Custom filter for JWT token processing in Spring Security.
 * <p>
 * This filter intercepts incoming HTTP requests to extract and validate JWT tokens. It extends
 * GenericFilterBean, making it a part of Spring Security's filter chain.
 */
@Component
public class JwtTokenFilter extends GenericFilterBean {
    /**
     * JwtTokenProvider is used to perform operations related to JWT tokens.
     */
    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Processes each HTTP request to extract and validate JWT tokens.
     * <p>
     * This method, part of the JwtTokenFilter, intercepts incoming HTTP requests to handle JWT token
     * processing. It's responsible for extracting the JWT token from the request, validating the token,
     * and setting the appropriate authentication in the security context of Spring Security.
     * <p>
     * Workflow:
     * 1. Extracts the JWT token from the request using the JwtTokenProvider.
     * 2. If a token is present, it validates the token. Valid tokens result in fetching the corresponding
     * Authentication object and setting it in the SecurityContextHolder, which integrates with Spring
     * Security's authentication mechanism.
     * 3. If the token is invalid (expired or malformed), it clears the security context to prevent
     * unauthorized access and sends an error response back to the client.
     * 4. Regardless of the token's validity, the filter chain continues its execution for other filters.
     *
     * @param servletRequest  The incoming HTTP request.
     * @param servletResponse The HTTP response object.
     * @param filterChain     The filter chain to which the request and response should be passed after processing.
     * @throws IOException      If an input or output exception occurs.
     * @throws ServletException If a servlet-specific error occurs.
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (JwtAuthenticationException e) {
            SecurityContextHolder.clearContext();
            ((HttpServletResponse) servletResponse).sendError(e.getHttpStatus().value());
            throw new JwtAuthenticationException("JWT token is expired or invalid");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
