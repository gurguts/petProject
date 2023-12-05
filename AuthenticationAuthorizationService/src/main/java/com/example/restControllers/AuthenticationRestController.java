package com.example.restControllers;

import com.example.dto.AuthenticationRequestDTO;
import com.example.models.User;
import com.example.security.JwtTokenProvider;
import com.example.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Rest Controller for handling authentication-related operations.
 * <p>
 * This controller manages the authentication process, including user login and logout. It's mapped to
 * the "/api/v1/auth" path and provides endpoints for login and logout functionalities.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationRestController {
    /**
     * AuthenticationManager is used to authenticate users.
     */
    private final AuthenticationManager authenticationManager;
    /**
     * UserService is used to define User
     */
    private final UserService userService;
    /**
     * JwtTokenProvider is used to create and manage JWT tokens.
     */
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationRestController(AuthenticationManager authenticationManager,
                                        UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Authenticates a user based on login credentials and issues a JWT token.
     * <p>
     * This endpoint handles POST requests for user authentication. The method
     * authenticates the user using Spring Security's AuthenticationManager. Upon successful authentication,
     * a JWT token is generated and returned to the user along with their login information.
     * <p>
     * If the authentication process fails (due to incorrect credentials, for instance), an
     * AuthenticationException is caught, and an appropriate error response is returned.
     *
     * @param requestDTO The data transfer object containing the user's login credentials.
     * @return A ResponseEntity containing the JWT token and user login information if authentication
     * is successful, or an error message if it fails. The HTTP status is set to OK (200)
     * for successful authentication, or FORBIDDEN (403) for failed authentication.
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDTO requestDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestDTO.getLogin(), requestDTO.getPassword()));
            User user = userService.findByLogin(requestDTO.getLogin())
                    .orElseThrow(() ->
                            new UsernameNotFoundException("User \"" + requestDTO.getLogin() + "\" doesn't exist"));
            String token = jwtTokenProvider.createToken(requestDTO.getLogin(), user.getRole().name());

            Map<Object, Object> response = new HashMap<>();
            response.put("login", requestDTO.getLogin());
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid login/password combination", HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Handles the user logout process by invalidating the authentication token.
     * <p>
     * This endpoint manages GET requests for user logout. It does not perform a traditional logout
     * by terminating a session since it's designed for a stateless authentication mechanism using JWT tokens.
     * Instead, it invalidates the JWT token by clearing the relevant cookie.
     * <p>
     * The @CrossOrigin annotation with specified origins and 'allowCredentials = true' allows handling
     * cross-origin requests in a specific manner, which is especially useful during development or
     * in environments where the front-end and back-end are served from different origins.
     *
     * @param httpServletResponse The HttpServletResponse to which the cookie modifications are applied.
     *                            This response will be sent back to the client with the updated cookie settings.
     */
    @GetMapping("/logout")
    @CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
    public void logout(HttpServletResponse httpServletResponse) {
        Cookie cookie = new Cookie("authToken", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
    }
}
