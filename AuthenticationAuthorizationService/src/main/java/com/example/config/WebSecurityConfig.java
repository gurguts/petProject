package com.example.config;

import com.example.security.JwtConfigure;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * This class configures the web security for the application.
 * <p>
 * It is annotated with @Configuration, which indicates that the class is a source of bean definitions.
 * The @EnableWebSecurity annotation switches off the default web security configuration and enables custom
 * security settings. The @EnableMethodSecurity annotation allows method level security.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    /**
     * A component that configures JWT token processing in the security filter chain.
     */
    private final JwtConfigure jwtConfigure;

    public WebSecurityConfig(JwtConfigure jwtConfigure) {
        this.jwtConfigure = jwtConfigure;
    }

    /**
     * Configures the security filter chain for the application.
     * <p>
     * This method defines the security rules and behavior for HTTP requests processed by the application.
     * It is part of the overall web security configuration for the application.
     * <p>
     * The configuration involves the following: - Disabling CSRF (Cross-Site Request Forgery)
     * protection since it's typically used in stateless REST APIs where CSRF tokens are not required.
     * - Setting the session management policy to stateless, indicating that no session will be maintained by the server.
     * - Authorizing requests based on specified URL patterns:
     * - Permitting all requests to certain URLs like login, registration, home page, static resources (CSS, JS).
     * - Requiring authentication for all other requests to ensure they are only accessible to authenticated users.
     * - Applying JWT (JSON Web Token) configuration to manage authentication and authorization using tokens.
     *
     * @param http The HttpSecurity object to be configured.
     * @return The configured SecurityFilterChain.
     * @throws Exception If an error occurs during the configuration process.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/login",
                                "/",
                                "/favicon.ico",
                                "/registration",
                                "/api/v1/reg/register",
                                "/css/**",
                                "/js/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .apply(jwtConfigure);
        return http.build();
    }

    /**
     * Creates and provides a bean of the PasswordEncoder type.
     * <p>
     * This method configures the password encoding mechanism for the application.
     * It is used to secure passwords by encoding them before storing them in the database.
     *
     * @return An instance of PasswordEncoder, specifically a BCryptPasswordEncoder.
     * The BCryptPasswordEncoder uses the BCrypt strong hashing function with a strength (or work factor) of 12.
     * This strength value determines the computational complexity of the hashing process,
     * making it more difficult for attackers to crack the hashed passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * Exposes the default AuthenticationManager as a Spring bean.
     * <p>
     * This method is used to create and provide an instance of AuthenticationManager,
     * which is a fundamental part of Spring Security's authentication system.
     * The AuthenticationManager is responsible for processing authentication requests.
     * This method allows for the AuthenticationManager to be injected and managed by Spring,
     * making it available for use throughout the application where authentication operations are required.
     *
     * @param authenticationConfiguration The AuthenticationConfiguration used to access the default AuthenticationManager.
     * @return The default AuthenticationManager instance provided by the framework.
     * @throws Exception If an error occurs during the retrieval of the AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
