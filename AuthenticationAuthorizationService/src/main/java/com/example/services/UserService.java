package com.example.services;

import com.example.dto.RegistrationRequestDTO;
import com.example.exception.InvalidLoginException;
import com.example.exception.InvalidPasswordException;
import com.example.models.Role;
import com.example.models.Status;
import com.example.models.User;
import com.example.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This class provides services related to user management such as finding users by login and registering new users.
 */
@Service
public class UserService {
    /**
     * UserRepository is used for database operations
     */
    private final UserRepository userRepository;
    /**
     * PasswordEncoder is used to encrypt passwords
     */
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * This method provides a way to find a user in the database based on their login identifier. It utilizes
     * the UserRepository to perform the search. If a user with the given login exists, an Optional containing
     * the User entity is returned. Otherwise, an empty Optional is returned, avoiding the need for null checks
     * and handling.
     *
     * @param login The login identifier of the user to be searched for.
     * @return An Optional<User> which, if a user is found, contains the User entity, otherwise an empty Optional.
     */
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public String registerUser(RegistrationRequestDTO requestDTO) {
        validateRegistrationData(requestDTO);

        User newUser = new User();
        newUser.setLogin(requestDTO.getLogin());
        newUser.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        newUser.setRole(Role.valueOf(requestDTO.getRole()));
        newUser.setStatus(Status.valueOf(requestDTO.getStatus()));

        userRepository.save(newUser);

        return "User registered successfully";
    }

    /**
     * This method is responsible for ensuring that the user registration data meets specific
     * criteria and business rules before allowing a new user to be registered in the system. It performs
     * several checks on the registration data:
     * <p>
     * 1. Uniqueness of Login: Verifies that the login provided in the requestDTO is not already in use
     * by another user in the system. If the login already exists, it throws an InvalidLoginException.
     * <p>
     * 2. Login and Password Distinction: Ensures that the login and password are not the same, as this
     * practice can be a security risk. If they are the same, an InvalidPasswordException is thrown.
     * <p>
     * 3. Password Complexity: Checks that the password meets certain complexity requirements (minimum length,
     * inclusion of numbers and special characters). This is done using a regular expression. If the password
     * does not meet these criteria, an InvalidPasswordException is thrown.
     *
     * @param requestDTO The data transfer object containing the user registration details.
     * @throws InvalidLoginException    If the login is already in use.
     * @throws InvalidPasswordException If the password criteria are not met.
     */
    private void validateRegistrationData(RegistrationRequestDTO requestDTO) {
        if (userRepository.existsByLogin(requestDTO.getLogin())) {
            throw new InvalidLoginException("Login is already in use");
        }

        if (requestDTO.getLogin().equals(requestDTO.getPassword())) {
            throw new InvalidPasswordException("Password cannot be the same as login");
        }

        String passwordRegex = "^(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$";
        if (!requestDTO.getPassword().matches(passwordRegex)) {
            throw new InvalidPasswordException(
                    "The password must have a minimum of 8 characters, including one number and one special character");
        }
    }
}
