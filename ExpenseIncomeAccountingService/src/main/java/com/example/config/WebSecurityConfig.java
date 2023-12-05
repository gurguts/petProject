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
import org.springframework.web.client.RestTemplate;

/**
 * This class configures the security settings for the application using Spring Security.
 * It sets up the security filter chain, CSRF protection, session management, authorization rules,
 * and additional security components like JWT configuration and password encoding.
 * <p>
 * The class is annotated with @Configuration, @EnableWebSecurity, and @EnableMethodSecurity to
 * indicate that it provides security configuration.
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
     * This method is responsible for setting up the security rules and behavior for handling HTTP requests.
     * The configuration involves several key aspects:
     * - Disabling CSRF (Cross-Site Request Forgery) protection, which is typically not needed for REST APIs.
     * - Setting the session management to stateless, indicating that the application does not maintain session state.
     * - Configuring request authorization rules. It specifies that requests to certain paths (like static
     * resources and favicon) are allowed without authentication, while all other requests require authentication.
     * - Applying the JwtConfigure to integrate JWT token processing in the security filter chain.
     * <p>
     * After configuring these aspects, the method builds and returns the SecurityFilterChain object,
     * which is then used by Spring Security to handle security concerns.
     *
     * @param http The HttpSecurity object to be configured with security settings.
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
                                "/favicon.ico",
                                "/css/**",
                                "/js/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .apply(jwtConfigure);
        return http.build();
    }

    /**
     * This method creates and returns a PasswordEncoder bean that is used throughout the application
     * for password encryption and verification. It specifically returns an instance of BCryptPasswordEncoder,
     * which uses the BCrypt hashing function to securely store passwords. The strength (or work factor) of
     * the BCrypt algorithm is set to 12, which is a good balance between security and performance.
     *
     * @return A PasswordEncoder bean that uses the BCrypt hashing function with a strength of 12.
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
     * @param authenticationConfiguration The AuthenticationConfiguration used to access the default
     *                                    AuthenticationManager.
     * @return The default AuthenticationManager instance provided by the framework.
     * @throws Exception If an error occurs during the retrieval of the AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * This method configures and returns a RestTemplate bean, which is a central class in Spring Framework for
     * client-side HTTP access. RestTemplate is used for consuming REST-ful web services and handles HTTP
     * requests and responses, simplifying the interaction with web services.
     *
     * @return A RestTemplate bean for HTTP operations.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
