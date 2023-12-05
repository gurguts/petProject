package com.example.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * Configuration component for integrating JWT token filtering into Spring Security.
 * <p>
 * This component extends SecurityConfigurerAdapter, allowing it to customize security configurations.
 * Specifically, it integrates a custom JWT token filter into the existing Spring Security filter chain.
 * The JWT token filter is responsible for intercepting HTTP requests and validating JWT tokens,
 * thereby enabling stateless authentication in the application.
 */
@Component
public class JwtConfigure extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    /**
     * Integration of native JWT token filter
     */
    private final JwtTokenFilter jwtTokenFilter;

    public JwtConfigure(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    /**
     * Configures the HttpSecurity object to include the JWT token filter.
     * <p>
     * This method overrides the configure method from SecurityConfigurerAdapter. It's used
     * to insert the custom JwtTokenFilter into the Spring Security filter chain. The JwtTokenFilter
     * is added before the UsernamePasswordAuthenticationFilter in the filter chain. This ensures
     * that the JWT token is processed and validated for each HTTP request before any authentication
     * logic provided by Spring Security is executed.
     * <p>
     * By placing the JwtTokenFilter before the UsernamePasswordAuthenticationFilter, it allows the
     * application to perform stateless authentication using JWT tokens, which is crucial for REST-ful APIs
     * where each request needs to be independently authenticated without maintaining session state.
     *
     * @param httpSecurity The HttpSecurity object that is used to configure the security features of the application.
     */
    @Override
    public void configure(HttpSecurity httpSecurity) {
        httpSecurity.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
