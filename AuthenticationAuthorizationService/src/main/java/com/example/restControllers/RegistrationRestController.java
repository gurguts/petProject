package com.example.restControllers;

import com.example.dto.RegistrationRequestDTO;
import com.example.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller responsible for handling user registration requests.
 * <p>
 * This controller provides an API endpoint for registering new users. It is mapped to
 * the base path "api/v1/reg" and includes methods to handle registration-related HTTP requests.
 */
@RestController
@RequestMapping("api/v1/reg")
public class RegistrationRestController {
    /**
     * UserService используется для выполнения регистрации фактической логики.
     */
    private final UserService userService;


    public RegistrationRestController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles the registration of a new user.
     * <p>
     * This method processes POST requests to the "/register" endpoint. It receives user registration
     * data encapsulated in a RegistrationRequestDTO object.
     * The method delegates the task of registering the user to the UserService.
     * <p>
     * On successful registration, it returns a response entity with an OK status (HTTP 200) and a message
     * indicating successful registration. If the registration data fails validation or any other
     * IllegalArgumentException is thrown during the registration process, the method catches this exception
     * and returns a response entity with a BAD REQUEST status (HTTP 400) along with the exception message.
     *
     * @param requestDTO The data transfer object containing the registration details of the new user.
     * @return A ResponseEntity containing either a success message or an error message, along with
     * the corresponding HTTP status code.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequestDTO requestDTO) {
        try {
            String response = userService.registerUser(requestDTO);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
